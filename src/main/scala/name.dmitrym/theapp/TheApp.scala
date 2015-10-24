package name.dmitrym.theapp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.codahale.metrics.MetricRegistry
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import name.dmitrym.theapp.routing._

import scala.io.StdIn

object TheApp extends App with LazyLogging {
  implicit val system = ActorSystem("theApp")
  implicit val materializer = ActorMaterializer()

  val metricRegistry = new MetricRegistry

  val route = pathPrefix("public") {
    getFromResourceDirectory("public/")
  } ~ path("public") {
    redirect("public/index.html", StatusCodes.MovedPermanently)
  } ~ pathSingleSlash {
    redirect("public/index.html", StatusCodes.MovedPermanently)
  } ~ pathPrefix("api") {
    pathPrefix("v0") {
      Sessions().route ~ Calendar.route ~ Users.route ~ Companies.route ~ Invoices.route ~ CompanyEvents.route
    }
  }

  val config = ConfigFactory.load("application.conf")
  assert(config.hasPath("service.interface"), "Config doesn't have service.interface specified")
  val listenInterface = config.getString("service.interface")
  assert(config.hasPath("service.port"), "Config doesn't have service.port specified")
  val listenPort = config.getInt("service.port")
  val bindingFuture = Http().bindAndHandle(route, listenInterface, listenPort)

  StdIn.readLine()

  import system.dispatcher
  bindingFuture.flatMap(_.unbind()).onComplete( _ => system.shutdown() )
}
