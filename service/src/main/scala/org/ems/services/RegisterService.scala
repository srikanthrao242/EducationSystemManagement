/**/
package org.ems.services

import akka.event.slf4j.SLF4JLogging
import org.ems.cm.services.CompanyService
import org.ems.entities.RegisterUser
import org.ems.um.database.UserSchema
import org.ems.um.services.UserService

import scala.concurrent.{ExecutionContext, Future}

class RegisterService(ec: ExecutionContext)
  extends SLF4JLogging
  with CompanyService
  with UserService
  with UserSchema {

  def registerUser(registerUser: RegisterUser): Future[(Int, Int)] =
    for {
      company <- addCompany(registerUser.company)
      user <- addUser(registerUser.user.copy(`companyid` = Some(company))).recoverWith {
        case ex: Exception =>
          log.error(
            s"Got error while adding user so deleting company with id $company"
          )
          for {
            v <- deleteCompany(company)
          } yield v
          throw ex
      }
      _ <- createSchema(user)
    } yield {
      log.debug(s"added company and user with $company, $user")
      (company, user)
    }

  override implicit val executor: ExecutionContext = ec
}
