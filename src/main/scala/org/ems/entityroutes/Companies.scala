/**/
package org.ems.entityroutes

import akka.actor._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import spray.json._
import akka.event.slf4j.SLF4JLogging

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import DefaultJsonProtocol._
import org.ems.AkkaCoreModule
import org.ems.cm.entities._
import org.ems.cm.CompaniesSystem
import org.ems.services.CompanyService
trait Companies extends RouteConcatenation with SLF4JLogging {
  this: AkkaCoreModule =>

  import org.ems.cm.entities.CompanySer._
  val companyActor: ActorRef =
    actorSystem.actorOf(Props[CompaniesSystem], "company")
  val companyService = new CompanyService(companyActor)
  val companyRoute: Route = {
    pathPrefix("companies") {
      path(IntNumber) { id =>
        get {
          log.debug(s"Got get request for id $id")
          complete(companyService.getCompany(id).map(v => v))
        } ~
        delete {
          log.debug(s"Got request for delete for id $id")
          complete(companyService.deleteCompany(id).map(v => v.toString))
        }
      } ~
      post {
        entity(as[Company]) { comp: Company =>
          log.debug(s"Got request for insert $comp")
          complete(companyService.addCompany(comp).map(_.toString()))
        }
      } ~
      put {
        entity(as[Company]) { comp: Company =>
          log.debug(s"Got request for put for $comp")
          complete(companyService.updateCompany(comp).map(_.toString()))
        }
      } ~
      get {
        log.debug(s"Got request for get for companies")
        complete(
          companyService.getAllCompanies
        )
      }
    }
  }
}
