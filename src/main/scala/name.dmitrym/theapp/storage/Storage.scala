package name.dmitrym.theapp.models

import com.typesafe.config.Config
import org.mongodb.scala.MongoClient

class Storage private(mongoUri: String) {
  private[this] val mongoClient = MongoClient(mongoUri)
  private[this] val database = mongoClient.getDatabase("theapp")

  val companies = database.getCollection("companies")
  val users = database.getCollection("users")
}

object Storage {
  def apply(cfg: Config):Storage = {
    assert(cfg.hasPath("mongo.uri"), "Config doesn't have mongo.uri specified")
    new Storage(cfg.getString("mongo.uri"))
  }
}
