package name.dmitrym.theapp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.codahale.metrics.MetricRegistry
import com.typesafe.scalalogging.LazyLogging
import name.dmitrym.theapp.routing._
import name.dmitrym.theapp.utils.Configuration

import scala.concurrent.Future

object TheApp extends App with LazyLogging {
  implicit val system = ActorSystem("theApp")
  implicit val materializer = ActorMaterializer()

  val metricRegistry = new MetricRegistry

  def start() = {
    val route = pathPrefix("public") {
      getFromResourceDirectory("public/")
    } ~ path("public") {
      redirect("public/index.html", StatusCodes.MovedPermanently)
    } ~ pathSingleSlash {
      redirect("public/index.html", StatusCodes.MovedPermanently)
    } ~ pathPrefix("api") {
      pathPrefix("v0") {
        Sessions().route ~ Calendar().route ~ Users().route ~ Companies().route ~ Invoices().route ~ CompanyEvents().route
      }
    }

    bindingFuture = Http().bindAndHandle(route, Configuration.listenInterface, Configuration.listenPort)
    bindingFuture
  }

  private[this] var bindingFuture:Future[ServerBinding] = start()

  def stop(): Unit = {
    import system.dispatcher
    bindingFuture.flatMap(_.unbind()).onComplete( _ => system.shutdown() )
  }
}
