package name.dmitrym.theapp

import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.HttpsContext
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.StatusCodes._
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
    val route = pathPrefix("webjars") {
      getFromResourceDirectory("META-INF/resources/webjars")
    } ~ pathPrefix("public") {
      getFromResourceDirectory("public/")
    } ~ path("public") {
      redirect("public/index.html", MovedPermanently)
    } ~ pathSingleSlash {
      redirect("public/index.html", MovedPermanently)
    } ~ pathPrefix("api") {
      pathPrefix("v0") {
        Sessions().route ~ Calendar().route ~ Users().route ~ Companies().route ~ Invoices().route ~ CompanyEvents().route
      }
    }

    val ksPath = Configuration.keyStorage
    val ksPass = Configuration.keyStoragePass.toCharArray

    val ks = KeyStore.getInstance("pkcs12", "SunJSSE")
    val is = getClass.getClassLoader.getResourceAsStream(ksPath)
    ks.load(is, ksPass)

    val kmf = KeyManagerFactory.getInstance("SunX509", "SunJSSE")
    kmf.init(ks, ksPass)

    val context = SSLContext.getInstance("TLS")
    context.init(kmf.getKeyManagers, null, new SecureRandom)

    bindingFuture = Http().bindAndHandle(route, Configuration.listenInterface, Configuration.listenPort, httpsContext = Some(HttpsContext(context)))
    bindingFuture
  }

  private[this] var bindingFuture:Future[ServerBinding] = start()

  def stop(): Unit = {
    import system.dispatcher
    bindingFuture.flatMap(_.unbind()).onComplete( _ => system.shutdown() )
  }
}
