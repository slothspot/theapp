package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.mongodb.util.JSON
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import name.dmitrym.theapp.models.{CompanyInfo, Company, Storage}
import com.softwaremill.session.SessionDirectives._
import com.mongodb.casbah.Imports._
import name.dmitrym.theapp.utils.Marshallers._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._

class Companies(implicit mat: ActorMaterializer) extends Router with LazyLogging {
  import Sessions.sessionManager
  private[this] val storage = Storage(ConfigFactory.load("application.conf"))

  private[this] val createCompanyTimer = metrics.timer("createCompanyTimer")
  val createCompany = createCompanyTimer.time { put { requiredSession() { session =>
    (requestEntityPresent & entity(as[CompanyItem])) { (companyItem) =>
      storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
        case None => complete(Responses.NotAuthorized)
        case Some(s) =>
          s.getAs[Int]("role") match {
            case Some(0) =>
              storage.companies.findOne(MongoDBObject("company.id" -> companyItem.company.id)) match {
                case None =>
                  storage.companies.insert(JSON.parse(companyItem.toJson.toString).asInstanceOf[DBObject])
                  complete(Responses.Ok)
                case Some(c) =>
                  complete(Responses.AlreadyExists)
              }
            case _ => complete(Responses.NotAllowed)
          }
      }
    }
  }}}

  private[this] val updateCompanyTimer = metrics.timer("updateCompanyTimer")
  val updateCompany = updateCompanyTimer.time { post { requiredSession() { session =>
    (requestEntityPresent & entity(as[CompanyItem])) {  (companyItem) =>
      storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
        case None => complete(Responses.NotAuthorized)
        case Some(s) =>
          s.getAs[Int]("role") match {
            case Some(0) =>
              storage.companies.findOne(MongoDBObject("company.id" -> companyItem.company.id)) match {
                case None => complete(Responses.DoesntExist)
                case Some(c) =>
                  storage.companies.update(c, JSON.parse(companyItem.toJson.toString).asInstanceOf[DBObject])
                  complete(Responses.Ok)
              }
            case Some(1) =>
              if(s.getAs[String]("companyId").get == companyItem.company.id) {
                storage.companies.findOne(MongoDBObject("company.id" -> companyItem.company.id)) match {
                  case None => complete(Responses.DoesntExist)
                  case Some(c) =>
                    storage.companies.update(c, JSON.parse(companyItem.toJson.toString).asInstanceOf[DBObject])
                    complete(Responses.Ok)
                }
              } else {
                complete(Responses.NotAllowed)
              }
            case _ => complete(Responses.NotAllowed)
          }
      }
    }
  }}}

  private[this] val companiesInfoTimer = metrics.timer("companiesInfoTimer")
  val companiesInfo = companiesInfoTimer.time { get { requiredSession() { session =>
    storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
      case Some(s) =>
        complete {
          s.get("role").asInstanceOf[Int] match {
            case 0 =>
              storage.companies.find().map { c =>
                JSON.serialize(c).parseJson.convertTo[CompanyItem].toJson.toString
              }.toArray.flatten.mkString("[", ",", "]")
            case 1 =>
              storage.companies.findOne(MongoDBObject("company.id" -> s.getAs[String]("companyId").get)) match {
                case Some(c) => JSON.serialize(c).parseJson.convertTo[CompanyItem].toJson.toString
                case None => Responses.DoesntExist
              }
            case _ => Responses.NotAllowed
          }
        }
      case None => complete(Responses.NotAuthorized)
    }
  }}}

  def route = path("companies") {
    createCompany ~ updateCompany ~ companiesInfo
  }
}

object Companies {
  def apply()(implicit materializer: ActorMaterializer) = new Companies
}