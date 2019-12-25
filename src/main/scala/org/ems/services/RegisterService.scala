/**/
package org.ems.services

import akka.event.slf4j.SLF4JLogging
import org.ems.entities.RegisterUser

import scala.concurrent.{ExecutionContext, Future}

class RegisterService(userService: UserService, companyService : CompanyService)(implicit ec: ExecutionContext) extends SLF4JLogging{

  def registerUser(registerUser: RegisterUser): Future[(Int,Int)] ={
    for{
      company <- companyService.addCompany(registerUser.company)
      user <- userService.addUser(registerUser.user.copy(`companyid` = Some(company)))
    }yield {
      log.debug(s"added company and user with $company, $user")
      (company, user)
    }
  }

}
