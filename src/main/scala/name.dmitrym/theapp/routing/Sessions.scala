package name.dmitrym.theapp.routing

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.mongodb.casbah.commons.MongoDBObject
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.{SessionManager, SessionUtil, SessionConfig}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import name.dmitrym.theapp.models.Storage
import name.dmitrym.theapp.utils.Marshallers._
import org.bson.types.ObjectId

class Sessions(implicit mat:ActorMaterializer) extends Router with LazyLogging {
  import Sessions.sessionManager
  private[this] val config = ConfigFactory.load("application.conf")
  private[this] val storage = Storage(config)

  private[this] val loginTimer = metrics.timer("login")
  val login = loginTimer.time { post {
    entity(as[LoginPayload]) { lp =>
      logger.debug("received lp.login => " + lp.login + " ; lp.password => " + lp.password)
      if(lp.login == config.getString("defaults.admin.login") && lp.password == config.getString("defaults.admin.password")) {
        storage.users.findOne(MongoDBObject("role" -> 0)) match {
          case Some(u) => complete(Responses.Fail("Not a first admin authorization")) // found main admin in users collection
          case None => complete(Responses.AdminFirstTime)
        }
      } else {
        storage.users.findOne(MongoDBObject("login" -> lp.login, "password" -> lp.password)) match {
          case Some(u) =>
            val sessionId = UUID.randomUUID().toString
            storage.sessions.insert(MongoDBObject("sessionId" -> sessionId, "userId" -> u.get("_id"), "role" -> u.get("role"), "company" -> u.get("companyId")))
            setSession(sessionId) { ctx =>
              ctx.complete {
                LoginResponsePayload(
                  u.get("_id").asInstanceOf[ObjectId].toHexString,
                  u.get("name").asInstanceOf[String],
                  u.get("role").asInstanceOf[Int]
                )
              }
            }
          case None =>
            complete(Responses.Fail("User doesn't exist"))
        }
      }
    }
  }}

  private[this] val logoutTimer = metrics.timer("logout")
  val logout = logoutTimer.time { get {
    requiredSession() { session =>
      invalidateSession() {
        storage.sessions.remove(MongoDBObject("sessionId" -> session))
        complete(Responses.Ok)
      }
    }
  }}

  def route = path("sessions") {
    login ~ logout
  }
}

object Sessions {
  private[this] val sessionConfig = SessionConfig.default(SessionUtil.randomServerSecret).withClientSessionEncryptData(true)
  implicit val sessionManager = new SessionManager[String](sessionConfig)
  def apply()(implicit  materializer: ActorMaterializer) = new Sessions
}