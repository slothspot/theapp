package name.dmitrym.theapp.routing

import name.dmitrym.theapp.models.{CompanyInfo, Company}

case class LoginPayload(companyId: String, userName: String, userPassword: String)

case class CompanyItem(company: Company, companyInfo: CompanyInfo)