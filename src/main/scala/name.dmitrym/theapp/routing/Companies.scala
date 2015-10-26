package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import name.dmitrym.theapp.models.{CompanyInfo, Company, Storage}
import com.softwaremill.session.SessionDirectives._
import com.mongodb.casbah.Imports._
import name.dmitrym.theapp.utils.Marshallers._
import spray.json._

class Companies(implicit mat: ActorMaterializer) extends Router with LazyLogging {
  import Sessions.sessionManager
  private[this] val storage = Storage(ConfigFactory.load("application.conf"))

  private[this] val createCompanyTimer = metrics.timer("createCompanyTimer")
  val createCompany = createCompanyTimer.time { put {
    complete(Responses.Stub)
  }}

  private[this] val updateCompanyTimer = metrics.timer("updateCompanyTimer")
  val updateCompany = updateCompanyTimer.time { post {
    complete(Responses.Stub)
  }}

  private[this] val companiesInfoTimer = metrics.timer("companiesInfoTimer")
  val companiesInfo = companiesInfoTimer.time { get { requiredSession() { session =>
    complete{storage.companies.find().map { o =>
      val company = Company(o.getAs[String]("id").get, o.getAs[String]("name").get)
      val companyInfo = CompanyInfo(
        o.getAs[String]("type").get,
        o.getAs[String]("domain").get,
        o.getAs[String]("web").get,
        o.getAs[String]("contactEmail").get,
        o.getAs[String]("address").get,
        o.getAs[String]("contactPhone").get
      )
      CompanyItem(company, companyInfo).toJson.toString()
    }.toArray.flatten.mkString("[", ",", "]")}
  }}}

  def route = path("companies") {
    createCompany ~ updateCompany ~ companiesInfo
  }
}

object Companies {
  def apply()(implicit materializer: ActorMaterializer) = new Companies
}