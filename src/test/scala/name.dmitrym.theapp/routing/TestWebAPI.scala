package name.dmitrym.theapp.routing

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import name.dmitrym.theapp.TheApp
import org.specs2.concurrent.ExecutionEnv
import org.specs2.matcher.Matcher
import org.specs2.mutable.Specification

import akka.stream.ActorMaterializer
import akka.http.scaladsl.model._
import akka.http.scaladsl.Http
import org.specs2.specification.BeforeAfterAll

import scala.concurrent.Await
import scala.concurrent.duration._

class TestWebAPI extends Specification with LazyLogging with BeforeAfterAll {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def beforeAll() {
    TheApp.main(Array[String]())
  }
  def afterAll() {
    TheApp.stop()
  }

  private[this] def beUserDoesntExist:Matcher[HttpResponse] = { r:HttpResponse =>
    val payload = Await.result(r.entity.dataBytes.runFold("")((acc, e) => acc + e.utf8String), 5.seconds)
    (payload == "{ \"result\" : \"error\", \"reason\" : \"User doesn't exist\" }", "Payload differs from expected")
  }

  private[this] def beNeedsSetup:Matcher[HttpResponse] = { r:HttpResponse =>
    val payload = Await.result(r.entity.dataBytes.runFold("")((acc, e) => acc + e.utf8String), 5.seconds)
    (payload == "{ \"result\" : \"ok\", \"needsSetup\" : true }", "Payload differs from expected")
  }

  "Sessions routing" should {
    "fail for invalid user" in { implicit ee: ExecutionEnv =>
      Http().singleRequest(HttpRequest(
        uri = "http://localhost:8080/api/v0/sessions",
        method = HttpMethods.POST,
        entity = HttpEntity(ContentType(MediaTypes.`application/json`), "{\"login\" : \"fakeLogin\", \"password\" : \"fakePasswordwithoutHash\"}")
      )) must beUserDoesntExist.awaitFor(30.seconds)
    }
    "succeeded for admin first time" in { implicit ee: ExecutionEnv =>
      Http().singleRequest(HttpRequest(
        uri = "http://localhost:8080/api/v0/sessions",
        method = HttpMethods.POST,
        entity = HttpEntity(ContentType(MediaTypes.`application/json`), "{\"login\" : \"admin\", \"password\" : \"21232f297a57a5a743894a0e4a801fc3\"}")
      )) must beNeedsSetup.awaitFor(30.seconds)
    }
    "succeeded for valid user" in {
      pending
    }
  }
}
