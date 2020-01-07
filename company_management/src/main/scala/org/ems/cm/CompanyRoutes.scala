/**/
package org.ems.cm

import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.ems.cm.entities.{Activation, Company}
import org.ems.cm.services.CompanyService
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.ems.cm.entities.CompanySer._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future

trait CompanyRoutes extends SLF4JLogging with CompanyService {

  val companyRoute: Route = {
    path("companies") {
      post {
        entity(as[Company]) { comp: Company =>
          log.debug(s"Got request for insert $comp")
          complete(addCompany(comp).map(_.toString()))
        }
      } ~
      put {
        entity(as[Company]) { comp: Company =>
          log.debug(s"Got request for put for $comp")
          complete(updateCompany(comp).map(_.toString()))
        }
      } ~
      get {
        log.debug(s"Got request for get for companies")
        complete(
          getAllCompanies
        )
      }
    } ~ pathPrefix("companies" / IntNumber) { id =>
      concat(
        put {
          pathSuffix("activation") {
            entity(as[Activation]) { act =>
              log.debug(s"Got request activation $act")
              complete(activation(id, act).map(_.toString))
            }
          }
        } ~
        get {
          log.debug(s"Got get request for id $id")
          complete(getCompany(id).map(v => v))
        } ~
        delete {
          log.debug(s"Got request for delete for id $id")
          complete(deleteCompany(id).map(v => v.toString))
        }
      )
    }
  }

}
