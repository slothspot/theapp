package name.dmitrym.theapp.utils

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

object Configuration extends LazyLogging {
  private[this] val cfg = {
    if(ConfigFactory.parseResources("application-dev.conf") == ConfigFactory.empty()) {
      logger.debug("Loading production configuration")
      ConfigFactory.load("application.conf")
    } else {
      logger.debug("Loading test configuration")
      ConfigFactory.load("application-dev.conf")
    }
  }

  val listenInterface = cfg.getString("service.interface")
  val listenPort = cfg.getInt("service.port")

  val defaultAdminLogin = cfg.getString("defaults.admin.login")
  val defaultAdminPassHash = cfg.getString("defaults.admin.passhash")

  val keyStorage = cfg.getString("service.security.keystorage")
  val keyStoragePass = cfg.getString("service.security.keystoragepass")

  val dbUri = cfg.getString("mongo.uri")
  val dbName = cfg.getString("mongo.db")
}
