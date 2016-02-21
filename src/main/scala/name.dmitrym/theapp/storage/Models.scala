package name.dmitrym.theapp.storage

import name.dmitrym.theapp.storage.EventType.EventType
import name.dmitrym.theapp.storage.InvoiceType.InvoiceType
import name.dmitrym.theapp.storage.NotificationType.NotificationType
import name.dmitrym.theapp.storage.UserRole.UserRole
import org.joda.time.DateTime
import spray.json.{JsObject, JsValue}

object UserRole extends Enumeration {
  type UserRole = Value
  val Provider, Seller, Supervisor, Admin, GlobalAdmin = Value
}

case class Company(id: String, name: String)
case class CompanyInfo(`type`: String, domain: Int, web: String, contactEmail: String, address: String, contactPhone: String)


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

// Notifications

object NotificationType extends Enumeration {
  type NotificationType = Value
  val Create, Update = Value
}

trait Notification {
  val date:DateTime
  val notificationType:NotificationType
  def diff:Seq[Change]
}

case class CreateNotification(newV: JsObject) extends Notification {
  override val date: DateTime = DateTime.now()
  override val notificationType: NotificationType = NotificationType.Create
  override def diff: Seq[Change] = {
    newV.fields.map { case (k, v) =>
      ChangeAdd(k, v)
    }.toSeq
  }
}

case class UpdateNotification(oldV: JsObject, newV: JsObject) extends Notification {
  override val date: DateTime = DateTime.now()
  override val notificationType: NotificationType = NotificationType.Update
  override def diff:Seq[Change] = {
    val oldF = oldV.fields
    val newF = newV.fields

    val addedKeys = newF.keys.filterNot(k => oldF.contains(k))
    val removedKeys = oldF.keys.filterNot(k => newF.contains(k))
    val modifiedKeys = newF.filter{ case (k,v) =>
      oldF.contains(k) && (oldF(k) != v)
    }.keys

    addedKeys.map { k =>
      ChangeAdd(k, newF(k))
    }.toSeq ++
    removedKeys.map { k =>
      ChangeDel(k)
    }.toSeq ++
    modifiedKeys.map { k =>
      ChangeMod(k, oldF(k), newF(k))
    }.toSeq
  }
}

abstract class Change {
  def k: String
}
case class ChangeAdd(k: String, v: JsValue) extends Change
case class ChangeMod(k: String, oldV: JsValue, newV: JsValue) extends Change
case class ChangeDel(k: String) extends Change
