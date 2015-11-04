package name.dmitrym.theapp.routing

import name.dmitrym.theapp.models.{CompanyInfo, Company}

case class LoginPayload(companyId: String, userName: String, userPassword: String)

case class CompanyItem(company: Company, companyInfo: CompanyInfo)

case class InvoiceCreatePayload(companyId: String, userId: String, title: String, reqType: String, reqNeed: String, reqDescription: String, reqImg: String, priority: Int)

case class InvoiceUpdatePayload(id: String, title: String, reqType: String, reqNeed: String, reqDescription: String,
  reqImg: String, startTime: String, finishTime: String, status: Int, responsible: String, result: String)