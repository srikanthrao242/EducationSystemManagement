/**/
package org.ems.em.entities

import java.sql.Timestamp

import spray.json._
import DefaultJsonProtocol._
import org.ems.em.entities

object Months extends Enumeration {
  type Months = Value
  val JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC = Value
}

object EmployeeType extends Enumeration {
  type EmployeeType = Value
  val PERMANENT, TRAINEE = Value
}

import Months._
import EmployeeType._

case class Employee(
  id: Option[Int],
  firstName: String,
  middleName: Option[String],
  lastName: Option[String],
  gender: String,
  dateOfJoining: Timestamp,
  dateOfRelieving: Option[Timestamp],
  email: String,
  mobile: String,
  city: String,
  address: String,
  companyId: Int,
  designation: String,
  employeeType: String,
  qualification: String
)

case class Salary(id: Option[Int],
                  employeeId: Int,
                  salaryPerHour: Double,
                  salaryPerMon: Double,
                  allowance: Double,
                  allowanceDesc: String,
                  deduction: Double,
                  deductionDesc: String,
                  taxPercentage: Double,
                  salAfterTax: Double,
                  salBeforeTax: Double,
                  tax: Double,
                  comments: String)

case class BankDetails(id: Option[Int],
                       employeeId: Int,
                       bankName: String,
                       branchCode: String,
                       accNo: String)

case class CreditSalary(
  id: Option[Int],
  employeeId: Int,
  creditedSalary: Double,
  presentDays: Double,
  workingDays: Double,
  month: String,
  year: Int,
  dateOfCredit: Timestamp
)

object EmployeesSer {

  implicit def enumFormat[T <: Enumeration](
    implicit enu: T
  ): RootJsonFormat[T#Value] =
    new RootJsonFormat[T#Value] {
      def write(obj: T#Value): JsValue = JsString(obj.toString)
      def read(json: JsValue): T#Value =
        json match {
          case JsString(txt) => enu.withName(txt)
          case somethingElse =>
            throw DeserializationException(
              s"Expected a value from enum $enu instead of $somethingElse"
            )
        }
    }
  implicit object DateJsonFormat extends RootJsonFormat[Timestamp] {
    override def write(obj: Timestamp): JsString = JsString(obj.toString)
    override def read(json: JsValue): Timestamp = json match {
      case JsString(s) => Timestamp.valueOf(s)
      case _ =>
        throw new DeserializationException("Error info you want here ...")
    }
  }

  implicit val monthsF: RootJsonFormat[entities.Months.Value] = enumFormat(Months)
  implicit val employeeTypeF: RootJsonFormat[entities.EmployeeType.Value] = enumFormat(EmployeeType)
  implicit val employeeF: RootJsonFormat[Employee] = jsonFormat15(Employee)
  implicit val salaryF: RootJsonFormat[Salary] = jsonFormat13(Salary)
  implicit val bankDetailsF: RootJsonFormat[BankDetails] = jsonFormat5(BankDetails)
  implicit val creditSalaryF: RootJsonFormat[CreditSalary] = jsonFormat8(CreditSalary)

}
