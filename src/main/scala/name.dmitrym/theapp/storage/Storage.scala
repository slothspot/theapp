package name.dmitrym.theapp.storage

import com.mongodb.casbah.{MongoClientURI, MongoClient}
import com.typesafe.scalalogging.LazyLogging
import name.dmitrym.theapp.utils.Configuration

class Storage private(mongoUri: String, dbName: String) extends LazyLogging {
  logger.debug(s"New instance of Storage created with uri $mongoUri")
  private[this] val mongoClient = MongoClient(MongoClientURI(mongoUri))
  private[this] val database = mongoClient(dbName)

  val sessions = database("sessions")
  val companies = database("companies")
  val users = database("users")
  val invoices = database("invoices")
}

object Storage {
  private[this] val cache = Map[(String, String), Storage]()

  def apply():Storage = {
    cache.getOrElse(
      (Configuration.dbUri, Configuration.dbName),
      new Storage(Configuration.dbUri, Configuration.dbName))
  }
}
