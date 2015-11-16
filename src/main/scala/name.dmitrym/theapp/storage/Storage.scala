package name.dmitrym.theapp.models

import com.mongodb.casbah.{MongoClientURI, MongoClient}
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

class Storage private(mongoUri: String) extends LazyLogging {
  logger.debug(s"New instance of Storage created with uri $mongoUri")
  private[this] val mongoClient = MongoClient(MongoClientURI(mongoUri))
  private[this] val database = mongoClient("theapp")

  val sessions = database("sessions")
  val companies = database("companies")
  val users = database("users")
  val invoices = database("invoices")

  val oplog = database("oplog")
}

object Storage {
  private[this] val cache = Map[Config, Storage]()

  def apply(cfg: Config):Storage = {
    assert(cfg.hasPath("mongo.uri"), "Config doesn't have mongo.uri specified")
    cache.getOrElse(cfg, new Storage(cfg.getString("mongo.uri")))
  }
}
