/**/
package org.ems.entities

import org.ems.cm.entities.Company
import org.ems.um.entities.User
import org.ems.um.entities.UserSer._
import org.ems.cm.entities.CompanySer._
import spray.json._
import DefaultJsonProtocol._

case class RegisterUser (user: User, company: Company)

object RegisterUserSer{
  implicit val registerUserF: RootJsonFormat[RegisterUser] = jsonFormat2(RegisterUser)
}
