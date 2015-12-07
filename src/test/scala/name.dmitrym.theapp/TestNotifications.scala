package name.dmitrym.theapp

import org.specs2.mutable.Specification

import name.dmitrym.theapp.storage._
import spray.json.{JsNumber, JsString, JsObject}

class TestNotifications extends Specification {
  val obj1 = JsObject(("n", JsString("t")), ("k", JsNumber(18)), ("s", JsString("longVal")), ("x", JsNumber(42)))
  val obj2 = JsObject(("n", JsString("t")), ("k", JsNumber(15)), ("j", JsString("asdklas")), ("s", JsString("short")))

  "Notifications" should {
    "have proper type in CreateNotification" in {
      val cn = CreateNotification(obj1)
      cn.notificationType === NotificationType.Create
    }
    "generate a seq of ChangeAdd objects" in {
      val cn = CreateNotification(obj1)
      cn.diff === obj1.fields.map { case (k,v) =>
        ChangeAdd(k, v)
      }.toSeq
    }
    "have proper type in UpdateNotification" in {
      val un = UpdateNotification(obj1, obj2)
      un.notificationType === NotificationType.Update
    }
    "generate a proper diff" in {
      val un = UpdateNotification(obj1, obj2)
      un.diff === Seq(ChangeAdd("j",JsString("asdklas")), ChangeDel("x"), ChangeMod("k",JsNumber(18),JsNumber(15)), ChangeMod("s",JsString("longVal"),JsString("short")))
    }
  }
}
