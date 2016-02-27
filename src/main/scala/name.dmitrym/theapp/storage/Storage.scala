package name.dmitrym.theapp.storage

import com.mongodb.casbah.{MongoClient, MongoClientURI}
import com.mongodb.casbah.commons.conversions.scala._
import com.mongodb.casbah.gridfs.GridFS
import com.typesafe.scalalogging.LazyLogging
import name.dmitrym.theapp.utils.Configuration

class Storage private(mongoUri: String, dbName: String) extends LazyLogging {
  logger.debug(s"New instance of Storage created with uri $mongoUri")
  private[this] val mongoClient = MongoClient(MongoClientURI(mongoUri))
  private[this] val database = mongoClient(dbName)

  def dropDB() = mongoClient.dropDatabase(dbName)

  val sessions = database("sessions")
  val companies = database("companies")
  val users = database("users")
  val invoices = database("invoices")
  val attachments = GridFS(database)
  val events = database("events")
}

object Storage {
  RegisterJodaTimeConversionHelpers()
  private[this] val cache = Map[(String, String), Storage]()

  def apply():Storage = {
    cache.getOrElse(
      (Configuration.dbUri, Configuration.dbName),
      new Storage(Configuration.dbUri, Configuration.dbName))
  }
}
