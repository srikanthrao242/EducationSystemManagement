/**/
package org.ems.cm.services

import com.ems.utilities.companies.entities._
import org.ems.cm.database.ImportExportDao

import scala.concurrent.{ExecutionContext, Future}

trait CompanyService  {
  implicit val executor : ExecutionContext
  def getAllCompanies: Future[List[Company]] =
    for {
      companies <- ImportExportDao.findAllCompanies().unsafeToFuture()
    } yield companies

  def getCompany(id: Int): Future[Option[Company]] =
    for {
      comp <- ImportExportDao.findCompany(id).unsafeToFuture()
    } yield comp

  def deleteCompany(id: Int): Future[Int] =
    for {
      comp <- ImportExportDao.deleteCompany(id).unsafeToFuture()
    } yield comp

  def addCompany(compInput: Company): Future[Int] =
    for {
      comp <- ImportExportDao.insertCompany(compInput).unsafeToFuture()
    } yield comp

  def updateCompany(compInput: Company): Future[Int] = {
    if(compInput.id.isEmpty){
      throw new Exception("Id is mandatory Field for update")
    }else{
      for {
        comp <- ImportExportDao.updateCompany(compInput).unsafeToFuture()
      } yield comp
    }
  }

  def activation(id:Int, act:Activation): Future[Int] = {
    for{
      d <- ImportExportDao.doActivation(id, act.isActivate).unsafeToFuture()
    }yield d
  }

  def getCompanyProImage(id: Int): Future[Option[String]] =
    for{
      v <- ImportExportDao.getCompanyImage(id).unsafeToFuture()
    }yield v

}
