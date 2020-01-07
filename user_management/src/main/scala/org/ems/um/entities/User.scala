/**/
package org.ems.um.entities

import java.sql.Timestamp

import spray.json.DefaultJsonProtocol._
import spray.json._
case class User(address: Option[String],
                city: Option[String],
                name: Option[String],
                email: Option[String],
                mobile: Option[String],
                id: Option[Int],
                `registerdate`: Option[Timestamp],
                `registrationexp`: Option[Timestamp],
                `companyid`: Option[Int],
                `usertype`: Option[String],
                `profileimg`: Option[String],
                `signature`: Option[String],
                `createdby`: Option[Int],
                isActive: Option[Boolean],
                password: Option[String])


case object GetAllUsers
case class GetUser(id: Int)
case class DeleteUser(id: Int)
case class UpdateUser(user: User)
case class AddUser(user: User)
case class AddSubUser(user: User, createdBy: Int)
case class Authenticate(email: String, password: String)

case class CreateSchema(userId: Int)
case class CreateEmployee(schema:String)

case class Activation(isActivate: Boolean)

object UserSer {
  implicit object DateJsonFormat extends RootJsonFormat[Timestamp] {
    override def write(obj: Timestamp): JsString = JsString(obj.toString)
    override def read(json: JsValue) : Timestamp = json match {
      case JsString(s) => Timestamp.valueOf(s)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }
  implicit val userF: RootJsonFormat[User] = jsonFormat15(User)
  implicit val activationF: RootJsonFormat[Activation] = jsonFormat1(Activation)
  implicit val authenticateF: RootJsonFormat[Authenticate] = jsonFormat2(Authenticate)
}


