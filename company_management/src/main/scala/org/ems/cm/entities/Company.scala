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
                   isActive: Option[Boolean],
                   numberofdays: Option[Int])

case class InsertCompany(address: Option[String],
                         city: Option[String],
                         companyname: Option[String],
                         email: Option[String],
                         mobile: Option[String],
                         companylogo:Option[String],
                         whatsup:Option[String],
                         isActive: Option[Boolean],
                         numberofdays: Option[Int],
                         registerdate: Option[Timestamp],
                         registrationexp: Option[Timestamp])

case object GetAllCompanies
case class GetCompany(id: Int)
case class DeleteCompany(id: Int)
case class UpdateCompanies(company: Company)
case class AddCompanies(company: Company)
case class Activation(isActivate:Boolean)

object CompanySer {
  implicit object DateJsonFormat extends RootJsonFormat[Timestamp] {
    override def write(obj: Timestamp): JsString = JsString(obj.toString)
    override def read(json: JsValue) : Timestamp = json match {
      case JsString(s) => Timestamp.valueOf(s)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }
  implicit val companyF: RootJsonFormat[Company] = jsonFormat12(Company)
  implicit val insertCompanyF: RootJsonFormat[InsertCompany] = jsonFormat11(InsertCompany)
  implicit val activationF: RootJsonFormat[Activation] = jsonFormat1(Activation)
}
