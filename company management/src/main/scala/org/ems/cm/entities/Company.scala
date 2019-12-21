/**/
package org.ems.cm.entities

import java.sql.Timestamp

import spray.json._
import DefaultJsonProtocol._
case class Company(address: Option[String],
                   city: Option[String],
                   companyname: Option[String],
                   email: Option[String],
                   mobile: Option[String],
                   id: Option[Int],
                   registerdate: Option[Timestamp],
                   registrationexp: Option[Timestamp],
                   companylogo:Option[String],
                   whatsup:Option[String],
                   isActive: Option[Boolean])

case class InsertCompany(address: Option[String],
                         city: Option[String],
                         companyname: Option[String],
                         email: Option[String],
                         mobile: Option[String],
                         companylogo:Option[String],
                         whatsup:Option[String],
                           isActive: Option[Boolean])

case object GetAllCompanies
case class GetCompany(id: Int)
case class DeleteCompany(id: Int)
case class UpdateCompanies(company: Company)
case class AddCompanies(company: InsertCompany)

object CompanySer {
  implicit object DateJsonFormat extends RootJsonFormat[Timestamp] {
    override def write(obj: Timestamp): JsString = JsString(obj.toString)
    override def read(json: JsValue) : Timestamp = json match {
      case JsString(s) => Timestamp.valueOf(s)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }
  implicit val companyF: RootJsonFormat[Company] = jsonFormat11(Company)
  implicit val insertCompanyF: RootJsonFormat[InsertCompany] = jsonFormat8(InsertCompany)
}
