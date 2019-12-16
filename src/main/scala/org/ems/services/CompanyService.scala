/**/
package org.ems.services

import akka.actor.{Actor, ActorRef}
import akka.util.Timeout
import org.ems.entities.{AddCompanies, Company, DeleteCompany, GetAllCompanies, GetCompany, InsertCompany, UpdateCompanies}
import akka.actor._
import akka.pattern.ask

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class CompanyService(company: ActorRef)(implicit val ec: ExecutionContext) {
  implicit val timeout = Timeout(5 seconds)

  def getAllCompanies(): Future[List[Company]] =
    for {
      companies <- (company ? GetAllCompanies).mapTo[List[Company]]
    } yield companies

  def getCompany(id: Int): Future[Company] =
    for {
      comp <- (company ? GetCompany(id)).mapTo[Company]
    } yield comp

  def deleteCompany(id: Int): Future[Int] =
    for {
      comp <- (company ? DeleteCompany(id)).mapTo[Int]
    } yield comp

  def addCompany(compInput: InsertCompany): Future[Int] =
    for {
      comp <- (company ? AddCompanies(compInput)).mapTo[Int]
    } yield comp

  def updateCompany(compInput: Company): Future[Int] = {
    if(compInput.id.isEmpty){
      throw new Exception("Id is mandatory Field for update")
    }else{
      for {
        comp <- (company ? UpdateCompanies(compInput)).mapTo[Int]
      } yield comp
    }
  }

}
