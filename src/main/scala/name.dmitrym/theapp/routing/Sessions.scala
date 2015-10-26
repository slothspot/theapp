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

class Sessions(implicit mat:ActorMaterializer) extends Router with LazyLogging {
  import Sessions.sessionManager
  private[this] val storage = Storage(ConfigFactory.load("application.conf"))

  private[this] val loginTimer = metrics.timer("login")
  val login = loginTimer.time { post {
    entity(as[LoginPayload]) { lp =>
      logger.debug("received lp.cmpId => " + lp.companyId + " ; lp.user => " + lp.userName + " ; lp.pass => " + lp.userPassword)
      storage.companies.findOne(MongoDBObject("id" -> lp.companyId)) match {
        case Some(c) =>
          storage.users.findOne(MongoDBObject("companyId" -> lp.companyId, "userName" -> lp.userName, "userPassHash" -> lp.userPassword)) match {
            case Some(u) =>
              val sessionId = UUID.randomUUID().toString
              storage.sessions.insert(MongoDBObject("sessionId" -> sessionId, "userId" -> u.get("_id")))
              setSession(sessionId) { ctx =>
                ctx.complete(Responses.Ok)
              }
            case None =>
              complete(Responses.Fail("User doesn't exist"))
          }
        case None =>
          complete(Responses.Fail("Company doesn't exist"))
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