package name.dmitrym.theapp.utils

import name.dmitrym.theapp.storage.{CompanyInfo, Company}
import name.dmitrym.theapp.routing._
import spray.json._

object Marshallers extends DefaultJsonProtocol {
  implicit val loginPayloadJSONMarshaller = jsonFormat2(LoginPayload)
  implicit val loginResponsePayloadJSONMarshaller = jsonFormat5(LoginResponsePayload)

  implicit val companyJSONMarshaller = jsonFormat2(Company)

  implicit val companyInfoJSONMarshaller = jsonFormat6(CompanyInfo)

  implicit val companyItemJSONMarshaller = jsonFormat2(CompanyItem)

  implicit val userInfoJSONMarshaller = jsonFormat5(UserInfo)

  implicit val InvoiceCreatePayloadJSONMarshaller = jsonFormat9(InvoiceCreatePayload)

  implicit val InvoiceUpdatePayloadJSONMarshaller = jsonFormat12(InvoiceUpdatePayload)

  implicit val CreateUserPayloadJSONMarshaller = jsonFormat5(CreateUserPayload)

  implicit val UpdateUserPayloadJSONMarshaller = jsonFormat8(UpdateUserPayload)
}
