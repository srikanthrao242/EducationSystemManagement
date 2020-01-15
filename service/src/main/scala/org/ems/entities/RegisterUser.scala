/**/
package org.ems.entities

import com.ems.utilities.companies.entities.Company
import com.ems.utilities.companies.entities.CompanySer._
import spray.json._
import DefaultJsonProtocol._
import com.ems.utilities.users.entities.User
import com.ems.utilities.users.entities.UserSer._

case class RegisterUser (user: User, company: Company)
case class UploadImageRes(fileName: String)

object RegisterUserSer{
  implicit val registerUserF: RootJsonFormat[RegisterUser] = jsonFormat2(RegisterUser)
  implicit val uploadImageResF: RootJsonFormat[UploadImageRes] = jsonFormat1(UploadImageRes)
}
