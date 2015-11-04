package name.dmitrym.theapp.models

import com.mongodb.casbah.{MongoClientURI, MongoClient}
import com.typesafe.config.Config

class Storage private(mongoUri: String) {
  private[this] val mongoClient = MongoClient(MongoClientURI(mongoUri))
  private[this] val database = mongoClient("theapp")

  val sessions = database("sessions")
  val companies = database("companies")
  val users = database("users")
  val invoices = database("invoices")
}

object Storage {
  def apply(cfg: Config):Storage = {
    assert(cfg.hasPath("mongo.uri"), "Config doesn't have mongo.uri specified")
    new Storage(cfg.getString("mongo.uri"))
  }
}
