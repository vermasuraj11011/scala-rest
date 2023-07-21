package controllers

import akka.pattern.StatusReply.{Success, error}
import entity.{Name, User}
import play.api.libs.json.{JsError, JsValue, Json, Reads, Writes}

import javax.inject._
import play.api.mvc._
import repo.UserRepository

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(
                                val controllerComponents: ControllerComponents,
                                userRepo: UserRepository)(implicit ec: ExecutionContext)
  extends BaseController {
  implicit val nameWrites: Writes[Name] = Json.writes[Name]
  implicit val userWrites: Writes[User] = Json.writes[User]

  //  implicit val userWritable =
  implicit val nameReads: Reads[Name] = Json.reads[Name]
  implicit val userReads: Reads[User] = Json.reads[User]


  def showUser(): Action[AnyContent] = Action {
    implicit req =>
      println(req)
      //      val name = Name("Suraj", "Verma")
      val user = User(1, "suraj", "Verma", "surajgmail.com", "00.00.000.00")
      Ok(Json.toJson(user))
  }

  def getUser(id: Int): Action[AnyContent] = Action.async { implicit request =>
    userRepo.getUserById(id).map {
      case Some(user) => Ok(Json.toJson(user))
      case None => NotFound
    }
  }

  def getAllUsers(): Action[AnyContent] = Action.async {
    implicit request => {
      userRepo.getAllUsers.map { users =>
        val jsonUser = users.map(user => Json.toJson(user))
        Ok(Json.arr(jsonUser))
      }
    }
  }

  def addUser(): Action[JsValue] = Action(parse.json) {
    implicit req =>
      val user = req.body.validate[User]
      user.fold(
        error => {
          BadRequest(Json.obj("message" -> JsError.toJson(error)))
        },
        user => {
          userRepo.addUser(user)
          Ok(
            Json.obj(
              "message" -> "User added successfully",
              "user" -> Json.toJson(user)
            )
          )
        }
      )
  }

  def updateUser(id: Int): Action[JsValue] =
    Action(parse.json) {
      implicit req =>
        val user = req.body.validate[User]
        user.fold(
          error => {
            BadRequest(Json.obj("message" -> JsError.toJson(error)))
          },
          user => {
            userRepo.updateUser(id,user)
            Ok(
              Json.obj(
                "message" -> "User updated successfully",
                "user" -> Json.toJson(user)
              )
            )
          }
        )
    }

  def deleteUser(id: Int): Action[AnyContent] = Action.async { implicit request =>
    userRepo.deleteUser(id).map(_ => Ok("User deleted successfully"))
  }

    def createUser(): Action[JsValue] = Action(parse.json) {
      req =>
        val user = req.body.validate[User]
        user.fold(
          errors => {
            BadRequest(Json.obj("message" -> JsError.toJson(errors)))
          },
          userVal => {
            Ok(Json.toJson(userVal))
          }
        )
    }

}
