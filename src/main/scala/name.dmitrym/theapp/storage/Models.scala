package name.dmitrym.theapp.models

import akka.http.scaladsl.model.Uri
import name.dmitrym.theapp.models.EventType.EventType
import name.dmitrym.theapp.models.InvoiceType.InvoiceType
import name.dmitrym.theapp.models.UserRole.UserRole

object UserRole extends Enumeration {
  type UserRole = Value
  val Provider, Seller, Supervisor, Admin, GlobalAdmin = Value
}

case class Company(id: String, name: String)
case class CompanyInfo(`type`: String, domain: String, web: String, contactEmail: String, address: String, contactPhone: String)


abstract class User(name: String, passHash: String, company: Company, role: UserRole)
case class GeneralUser(name: String, passHash: String, company: Company, role: UserRole) extends User(name, passHash, company, role)
case class Curator(name: String, passHash: String, company: Company, role: UserRole, companies: Option[Seq[Company]]) extends User(name, passHash, company, role)

object InvoiceType extends Enumeration {
  type InvoiceType = Value
  val Order, Inquiry = Value
}

case class Invoice(`type`: InvoiceType, creator: User, assignee: User, description: String, qty: Int, reason: String, attachments: Option[Seq[String]])

object InvoiceStatus extends Enumeration {
  type InvoiceStatus = Value
  val Created, Confirmed, Updated, Completed = Value
}

trait InvoiceHistory
case class CreateInvoice() extends InvoiceHistory
case class ProcessInvoice() extends InvoiceHistory
case class FinishInvoice() extends InvoiceHistory

case class CompanyContactEvent(company: Company, user: User, `type`: EventType, details: String)
object EventType extends Enumeration {
  type EventType = Value
  val Call, Meeting, Note, Presentation, IncomingCall  = Value
}
