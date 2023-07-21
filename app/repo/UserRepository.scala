package repo

import entity.User
import org.mongodb.scala.bson.collection.Document
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UserRepository @Inject()(implicit ec: ExecutionContext) {
  private val usersCollection = MongoDB.getCollection("user")

  def addUser(user: User): Future[Unit] = {
    usersCollection
      .insertOne(
        Document(
          "id" -> user.id,
          "first_name" -> user.first_name,
          "last_name" -> user.last_name,
          "email" -> user.email,
          "ip_address" -> user.ip_address))
      .toFuture().map(_ => ())
  }

  def updateUser(id:Int, user:User): Future[Unit] = {
    val filter = equal("id",id)
    val update = combine(
      set("first_name" , user.first_name),
      set("last_name" , user.last_name),
      set("email" , user.email),
      set("ip_address" , user.ip_address)
    )
    usersCollection.updateOne(filter, update)
      .toFuture()
      .map { updateResult =>
        if (updateResult.getMatchedCount == 1) {
          ()
        } else {
          throw new Exception("User not found")
        }
      }
  }

  def getUserById(id: Int): Future[Option[User]] = {
    usersCollection
      .find(equal("id", id))
      .headOption()
      .map(_.map(doc =>
        User(
          doc.getInteger("id"),
          doc.getString("first_name"),
          doc.getString("last_name"),
          doc.getString("email"),
          doc.getString("ip_address"))))
  }

  def getAllUsers: Future[List[User]] = {
    usersCollection
      .find()
      .toFuture()
      .map(_.map(doc =>
        User(
          doc.getInteger("id"),
          doc.getString("first_name"),
          doc.getString("last_name"),
          doc.getString("email"),
          doc.getString("ip_address"))).toList)
  }

  def deleteUser(id: Int): Future[Unit] = {
    usersCollection.deleteOne(equal("id", id)).toFuture().map(_ => ())
  }
}
