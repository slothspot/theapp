package name.dmitrym.theapp.routing

object Responses {
  val Stub = "{ \"result\" : \"stub\"}"

  val Ok = "{ \"result\" : \"ok\" }"

  def Fail(reason: String) = "{ \"result\" : \"error\", \"reason\" : \"" + reason + "\" }"

  def InvoiceCreated(id: String) = "{ \"result\" : \"ok\", \"id\" : \"" + id + "\" }"

  def InvoiceUpdated(id: String) = "{ \"result\" : \"ok\", \"id\" : \"" + id + "\" }"

  def LoggedIn(userName: String) = "{\"result\" : \"ok\", \"name\": \"" + userName + "\" }"

  val AdminFirstTime = "{ \"result\" : \"ok\", \"needsSetup\" : true }"

  val NotAuthorized = Fail("Not authorized")

  val NotAllowed = Fail("Not allowed")
}
