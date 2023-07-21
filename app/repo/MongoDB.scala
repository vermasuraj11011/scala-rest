package repo

import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import scala.concurrent.{ExecutionContext, Future}

object MongoDB {
  private val mongoClient: MongoClient = MongoClient()
  private val database: MongoDatabase = mongoClient.getDatabase("local")

  def getCollection(name: String): MongoCollection[Document] = database.getCollection(name)
}
