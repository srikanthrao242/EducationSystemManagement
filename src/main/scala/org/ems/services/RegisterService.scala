/**/
package org.ems.services

import akka.event.slf4j.SLF4JLogging
import org.ems.entities.RegisterUser

import scala.concurrent.{ExecutionContext, Future}

class RegisterService(userService: UserService, companyService: CompanyService)(
  implicit ec: ExecutionContext
) extends SLF4JLogging {

  def registerUser(registerUser: RegisterUser): Future[(Int, Int)] =
    for {
      company <- companyService.addCompany(registerUser.company)
      user <- userService
        .addUser(registerUser.user.copy(`companyid` = Some(company)))
        .recoverWith {
          case ex: Exception =>
            log.error(s"Got error while adding user so deleting company with id $company")
            for {
              v <- companyService.deleteCompany(company)
            } yield v
            throw ex
        }
    } yield {
      log.debug(s"added company and user with $company, $user")
      (company, user)
    }

}
