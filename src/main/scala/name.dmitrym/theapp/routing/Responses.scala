package name.dmitrym.theapp.routing

object Responses {
  val Stub = "{ \"result\" : \"stub\"}"

  val Ok = "{ \"result\" : \"ok\" }"

  def Fail(reason: String) = "{ \"result\" : \"error\", \"reason\" : \"" + reason + "\" }"
}
