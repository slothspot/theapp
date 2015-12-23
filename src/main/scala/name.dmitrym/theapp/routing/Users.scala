package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import name.dmitrym.theapp.storage.{Company, Storage}
import com.mongodb.casbah.Imports._
import name.dmitrym.theapp.utils.Marshallers._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._

class Users(implicit mat: ActorMaterializer) extends Router with LazyLogging {
  import Sessions.sessionManager
  private[this] val storage = Storage()
  private[this] val createUserTimer = metrics.timer("createUser")
  val createUser = createUserTimer.time { put { requiredSession(oneOff, usingCookies) { session =>
    (requestEntityPresent & entity(as[CreateUserPayload])) { (pl) =>
    storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
      case Some(s) =>
        if(s.get("role").asInstanceOf[Int] == 0 || (s.get("role").asInstanceOf[Int] == 1 && s.get("companyId") == pl.companyId && pl.role >= 1)) {
          storage.users.findOne(MongoDBObject("login" -> pl.login)) match {
            case None =>
              storage.users.insert(MongoDBObject(
                "login" -> pl.login,
                "password" -> pl.password,
                "name" -> pl.name,
                "companyId" -> pl.companyId,
                "role" -> pl.role
              ))
              complete(Responses.Ok)
            case Some(u) =>
              complete(Responses.AlreadyExists)
          }
        } else {
          complete(Responses.NotAllowed)
        }
      case None => complete(Responses.NotAuthorized)
    }
  }}}}

  private[this] val updateUserTimer = metrics.timer("updateUser")
  val updateUser = updateUserTimer.time { post { requiredSession(oneOff, usingCookies) { session =>
    (requestEntityPresent & entity(as[CreateUserPayload])) { (pl) =>
      storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
        case Some(s) =>
          if(s.get("role").asInstanceOf[Int] == 0 || (s.get("role").asInstanceOf[Int] == 1 && s.get("companyId") == pl.companyId && pl.role >= 1)) {
            storage.users.findOne(MongoDBObject("login" -> pl.login)) match {
              case None =>
                complete(Responses.DoesntExist)
              case Some(u) =>
                storage.users.update(u, MongoDBObject(
                  "login" -> pl.login,
                  "password" -> pl.password,
                  "name" -> pl.name,
                  "companyId" -> pl.companyId,
                  "role" -> pl.role
                ))
                complete(Responses.Ok)
            }
          } else {
            complete(Responses.NotAllowed)
          }
        case None => complete(Responses.NotAuthorized)
      }
    }
  }}}

  private[this] val userStatsTimer = metrics.timer("userStats")
  val userStats = userStatsTimer.time { get { requiredSession(oneOff, usingCookies) { session =>
    storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
      case Some(s) =>
        complete { s.get("role").asInstanceOf[Int] match {
          case 0 =>
            storage.users.find().map{ u =>
              val c = storage.companies.findOne(MongoDBObject("id" -> u.getAs[String]("companyId"))).get.asInstanceOf[Company]
              UserInfo(u.getAs[String]("login").get, u.getAs[String]("name").get, c, u.getAs[Int]("role").get).toJson.toString
            }.toArray.flatten.mkString("[", ",", "]")
          case 1 =>
            storage.users.find(MongoDBObject("companyId" -> s.getAs[String]("company").get)).map{ u =>
              val c = storage.companies.findOne(MongoDBObject("id" -> u.getAs[String]("companyId"))).get.asInstanceOf[Company]
              UserInfo(u.getAs[String]("login").get, u.getAs[String]("name").get, c, u.getAs[Int]("role").get).toJson.toString
            }.toArray.flatten.mkString("[", ",", "]")
          case _ => Responses.NotAllowed
        }}
      case None => complete(Responses.NotAuthorized)
    }
  }}}

  def route = path("users") {
    createUser ~ updateUser ~ userStats
  }
}

object Users {
  def apply()(implicit materializer: ActorMaterializer) = new Users
}