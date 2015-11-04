package name.dmitrym.theapp.routing

import name.dmitrym.theapp.models.{CompanyInfo, Company}

case class LoginPayload(login: String, password: String)
case class LoginResponsePayload(id: String, name: String, role: Int)

case class CompanyItem(company: Company, companyInfo: CompanyInfo)

case class InvoiceCreatePayload(companyId: String, userId: String, title: String, reqType: String, reqNeed: String, reqDescription: String, reqImg: String, priority: Int)

case class InvoiceUpdatePayload(id: String, title: String, reqType: String, reqNeed: String, reqDescription: String,
  reqImg: String, startTime: String, finishTime: String, status: Int, responsible: String, result: String)