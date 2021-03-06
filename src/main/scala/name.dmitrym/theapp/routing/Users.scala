package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import name.dmitrym.theapp.storage.NotificationType._
import name.dmitrym.theapp.storage._
import com.mongodb.casbah.Imports._
import name.dmitrym.theapp.utils.Marshallers._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.mongodb.util.JSON
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
              val no = Notification.toMongoDB(
                CreateNotification(s.get("userId").asInstanceOf[ObjectId].toHexString, s.get("company").asInstanceOf[String], CreateUser, pl.toJson.asJsObject)
              )
              storage.events.insert(no)
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
    (requestEntityPresent & entity(as[UpdateUserPayload])) { (pl) =>
      storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
        case Some(s) =>
          if(s.get("role").asInstanceOf[Int] == 0) {
            storage.users.findOne(MongoDBObject("login" -> pl.login)) match {
              case None =>
                complete(Responses.DoesntExist)
              case Some(u) =>
                storage.users.update(u, MongoDBObject(
                  "login" -> pl.login,
                  "password" -> pl.password,
                  "name" -> pl.name,
                  "email" -> pl.email,
                  "phone" -> pl.phone,
                  "address" -> pl.address,
                  "companyId" -> u.getAs[String]("companyId"),
                  "role" -> pl.role
                ))
                val no = Notification.toMongoDB(
                  UpdateNotification(s.get("userId").asInstanceOf[ObjectId].toHexString, s.get("company").asInstanceOf[String], UpdateUser, JsonParser(JSON.serialize(u)).asJsObject, pl.toJson.asJsObject)
                )
                storage.events.insert(no)
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
            val users = storage.users.find().map{ u =>
              val cId = u.getAs[String]("companyId")
              val c = cId match {
                case None | Some("") => Company("", "N/A")
                case Some(id) => storage.companies.findOne(MongoDBObject("company.id" -> cId)) match {
                  case Some(cmp) => Company(cmp.getAs[String]("company.id").get, cmp.getAs[String]("company.name").get)
                  case None => Company(id, "Invalid")
                }
              }

              UserInfo(u.getAs[ObjectId]("_id").get.toString, u.getAs[String]("login").get, u.getAs[String]("name").get, c, u.getAs[Int]("role").get).toJson.toString
            }
            val usersArray = users.toArray
            val usersStr = usersArray.mkString("[", ",", "]")
            usersStr
          case _ =>
            storage.users.find(MongoDBObject("companyId" -> s.getAs[String]("company").get)).map{ u =>
              val cmp = storage.companies.findOne(MongoDBObject("company.id" -> u.getAs[String]("companyId"))).get
              val c = Company(cmp.getAs[String]("company.id").get, cmp.getAs[String]("company.name").get)
              UserInfo(u.getAs[ObjectId]("_id").get.toString, u.getAs[String]("login").get, u.getAs[String]("name").get, c, u.getAs[Int]("role").get).toJson.toString
            }.toArray.mkString("[", ",", "]")
        }}
      case None => complete(Responses.NotAuthorized)
    }
  }}}

  private[this] val getUserInfoTimer = metrics.timer("getUserInfo")
  val getUserInfo = getUserInfoTimer.time {
    get { requiredSession(oneOff, usingCookies) { session =>
      extractUnmatchedPath { userId =>
        storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
          case Some(s) =>
            storage.users.findOne(MongoDBObject("_id" -> new ObjectId(userId.tail.toString()))) match {
              case Some(u) =>
                val c = storage.companies.findOne(MongoDBObject("company.id" -> u.getAs[String]("companyId"))) match {
                  case Some(cmp) => Company(cmp.getAs[String]("company.id").get, cmp.getAs[String]("company.name").get)
                  case None => Company("", "N/A")
                }
                complete(UserInfo(u.getAs[ObjectId]("_id").get.toString, u.getAs[String]("login").get, u.getAs[String]("name").get, c, u.getAs[Int]("role").get).toJson)
              case None => complete(Responses.DoesntExist)
            }
          case None => complete(Responses.NotAuthorized)
        }
      }
    }}
  }

  def route = path("users") {
    createUser ~ updateUser ~ userStats
  } ~ pathPrefix("user") {
    getUserInfo
  }
}

object Users {
  def apply()(implicit materializer: ActorMaterializer) = new Users
}