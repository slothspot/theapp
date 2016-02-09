package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import com.typesafe.scalalogging.LazyLogging

import name.dmitrym.theapp.storage.Storage

class Stats(implicit mat: ActorMaterializer) extends Router with LazyLogging {
  import Sessions.sessionManager

  private[this] val getUsersStatsTimer = metrics.timer("getUsersStats")
  val getUsersStats = getUsersStatsTimer.time { get { requiredSession(oneOff, usingCookies) { session =>
    complete("{\"count\" : " + Storage().users.size + " }")
  }}}

  private[this] val getCompaniesStatsTimer = metrics.timer("getCompaniesStats")
  val getCompaniesStats = getCompaniesStatsTimer.time { get { requiredSession(oneOff, usingCookies) { session =>
    complete("{\"count\" : " + Storage().companies.size + " }")
  }}}

  private[this] val getInvoicesStatsTimer = metrics.timer("getInvoicesStats")
  val getInvoicesStats = getInvoicesStatsTimer.time { get { requiredSession(oneOff, usingCookies){ session =>
    complete("{\"count\" : " + Storage().invoices.size + " }")
  }}}

  def route = pathPrefix("stats") {
    path("users") {
      getUsersStats
    } ~ path("companies") {
      getCompaniesStats
    } ~ path("invoices") {
      getInvoicesStats
    }
  }
}

object Stats {
  def apply()(implicit materializer: ActorMaterializer) = new Stats
}
