package name.dmitrym.theapp.utils

import name.dmitrym.theapp.models.{CompanyInfo, Company}
import name.dmitrym.theapp.routing._
import spray.json._

object Marshallers extends DefaultJsonProtocol {
  implicit val loginPayloadJSONMarshaller = jsonFormat2(LoginPayload)
  implicit val loginResponsePayloadJSONMarshaller = jsonFormat3(LoginResponsePayload)

  implicit val companyJSONMarshaller = jsonFormat2(Company)

  implicit val companyInfoJSONMarshaller = jsonFormat6(CompanyInfo)

  implicit val companyItemJSONMarshaller = jsonFormat2(CompanyItem)

  implicit val InvoiceCreatePayloadJSONMarshaller = jsonFormat8(InvoiceCreatePayload)

  implicit val InvoiceUpdatePayloadJSONMarshaller = jsonFormat11(InvoiceUpdatePayload)
}