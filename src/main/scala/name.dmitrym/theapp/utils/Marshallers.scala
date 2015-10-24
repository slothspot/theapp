package name.dmitrym.theapp.utils

import name.dmitrym.theapp.models.Company
import name.dmitrym.theapp.routing.LoginPayload
import spray.json._

object Marshallers extends DefaultJsonProtocol {

  implicit object LoginPayloadJSONMarshaller extends RootJsonFormat[LoginPayload] {
    def read(value: JsValue) = {
      value.asJsObject.getFields("companyId", "userName", "userPassword") match {
        case Seq(JsString(companyId), JsString(userName), JsString(userPassword)) => new LoginPayload(companyId, userName, userPassword)
        case _ => throw new DeserializationException("LoginPayload expected")
      }
    }
    def write(lp: LoginPayload) = {
      JsObject(
        "companyId" -> JsString(lp.companyId),
        "userName" -> JsString(lp.userName),
        "userPassword" -> JsString(lp.userPassword)
      )
    }
  }
}