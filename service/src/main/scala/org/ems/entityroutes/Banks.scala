/**/
package org.ems.entityroutes

import akka.actor.Props
import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.RouteConcatenation
import org.ems.AkkaCoreModule
import org.ems.em.EmployeeSystem
import org.ems.services.BankServices
import akka.http.scaladsl.server.Directives._
import org.ems.em.entities._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import EmployeesSer._
import DefaultJsonProtocol._

trait Banks extends RouteConcatenation with SLF4JLogging {
  this: AkkaCoreModule =>

  val bankActor = actorSystem.actorOf(Props[EmployeeSystem], "Banks")
  val bankServices = new BankServices(bankActor)

  val bankRoute = pathPrefix("banks") {
    get {
      log.debug(s"Got request to get all banks details")
      complete(bankServices.doProcess[_, List[BankDetails]](GetAllBankDetails))
    } ~
    path(IntNumber) { id =>
      get {
        log.debug(s"Got request to get banks details by id $id")
        complete(
          bankServices.doProcess[_, Option[BankDetails]](GetBankDetails(id))
        )
      } ~
      delete {
        log.debug(s"Got request to delete bank details of id $id")
        complete(
          bankServices.doProcess[_, Int](DeleteBankDetails(id)).map(_.toString)
        )
      }
    } ~ post {
      entity(as[BankDetails]) { bank =>
        log.debug(s"Got request to add bank details $bank")
        complete(
          bankServices
            .doProcess[_, Int](AddEmployeeBankDetails(bank))
            .map(_.toString)
        )
      }
    } ~ put {
      entity(as[BankDetails]) { bank =>
        log.debug(s"Got request to update the bank details $bank")
        complete(
          bankServices
            .doProcess[_, Int](UpdateBankDetails(bank))
            .map(_.toString)
        )
      }
    }
  }
}
