/**/
package org.ems

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import org.ems.entities._

class CompaniesSystem extends Actor with SLF4JLogging {
  override def receive: Receive = {
    case AddCompanies(company: Company) =>
      log.debug(s"got company to add $company")

    case UpdateCompanies(company: Company) =>
      log.debug(s"got company to update $company")
    case GetAllCompanies =>
      log.debug(s"got message to get all companies")
    case _ =>
      log.debug(s"Got default message")
  }
}
