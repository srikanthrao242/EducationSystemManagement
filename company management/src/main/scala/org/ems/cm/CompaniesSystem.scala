/**/
package org.ems.cm

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import akka.pattern.pipe
import org.ems.cm.database.ImportExportDao
import org.ems.cm.entities._

class CompaniesSystem extends Actor with SLF4JLogging {
  implicit val ec = context.system.dispatcher
  override def receive: Receive = {
    case AddCompanies(company: Company) =>
      log.debug(s"got company to add $company")
      ImportExportDao.insertCompany(company).unsafeToFuture().pipeTo(sender)(self)
    case UpdateCompanies(company: Company) =>
      log.debug(s"got company to update $company")
      sender() ! ImportExportDao.updateCompany(company).unsafeToFuture()
    case GetAllCompanies =>
      log.debug(s"got message to get all companies")
      ImportExportDao.findAllCompanies().unsafeToFuture().pipeTo(sender)(self)
    case GetCompany(id) =>
      log.debug(s"got message to get compnay of $id")
      ImportExportDao.findCompany(id).unsafeToFuture().pipeTo(sender)(self)
    case DeleteCompany(id) =>
      log.debug(s"got message to get compnay of $id")
      ImportExportDao.deleteCompany(id).unsafeToFuture().pipeTo(sender)(self)
    case _ =>
      log.debug(s"Got default message")
  }
}
