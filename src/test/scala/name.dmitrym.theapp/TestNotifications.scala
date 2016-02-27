package name.dmitrym.theapp

import org.specs2.mutable.Specification

import name.dmitrym.theapp.storage._
import spray.json.{JsNumber, JsString, JsObject}

class TestNotifications extends Specification {
  val who = "some_action_creator"
  val obj1 = JsObject(("n", JsString("t")), ("k", JsNumber(18)), ("s", JsString("longVal")), ("x", JsNumber(42)))
  val obj2 = JsObject(("n", JsString("t")), ("k", JsNumber(15)), ("j", JsString("asdklas")), ("s", JsString("short")))

  "Notifications" should {
    "have proper type in CreateNotification" in {
      val cn = CreateNotification(who, NotificationType.CreateUser, obj1)
      cn.action === NotificationType.CreateUser
    }
    "generate a seq of ChangeAdd objects" in {
      val cn = CreateNotification(who, NotificationType.CreateUser, obj1)
      cn.diff === obj1.fields.map { case (k,v) =>
        ChangeAdd(k, v.toString)
      }.toSeq
    }
    "have proper type in UpdateNotification" in {
      val un = UpdateNotification(who, NotificationType.UpdateUser, obj1, obj2)
      un.action === NotificationType.UpdateUser
    }
    "generate a proper diff" in {
      val un = UpdateNotification(who, NotificationType.UpdateUser, obj1, obj2)
      un.diff === Seq(ChangeAdd("j",JsString("asdklas").toString), ChangeDel("x"), ChangeMod("k",JsNumber(18).toString,JsNumber(15).toString), ChangeMod("s",JsString("longVal").toString, JsString("short").toString))
    }
  }
}
