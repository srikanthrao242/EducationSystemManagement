/**/
package org.ems.entities

case class Company(id: Option[Int],
                   companyname: Option[String],
                   address: Option[String],
                   city: Option[String],
                   mobile: Option[String],
                   email: Option[String],
                   registerdate: Option[String],
                   registrationexp: Option[String])

case object GetAllCompanies
case class UpdateCompanies(company: Company)
case class AddCompanies(company: Company)

