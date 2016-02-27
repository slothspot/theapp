package name.dmitrym.theapp.routing

import name.dmitrym.theapp.storage.{CompanyInfo, Company}

case class LoginPayload(login: String, password: String)
case class LoginResponsePayload(id: String, login: String, name: String, role: Int, companyId: String)

case class CompanyItem(company: Company, companyInfo: CompanyInfo)

case class InvoiceCreatePayload(
  title: String,
  reqType: String,
  reqNeed: String,
  reqDescription: String,
  priority: Int,
  quantity: Int,
  assigneeId: String,
  creatorId: String,
  companyId: String
)

case class InvoiceUpdatePayload(
  id: String,
  title: String,
  reqType: String,
  reqNeed: String,
  reqDescription: String,
  priority: Int,
  quantity: Int,
  assigneeId: String,
  creatorId: String,
  companyId: String,
  creationTime: String,
  status: Int
)

case class CreateUserPayload(            login: String, password: String, name: String, companyId: String, role: Int)
case class UpdateUserPayload(id: String, login: String, password: String, name: String,                    role: Int, email: String, phone: String, address: String)

case class UserInfo(id: String, login: String, name: String, company: Company, role: Int)
