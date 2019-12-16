/**/
package org.ems.entityroutes
import akka.actor._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import org.ems.AkkaCoreModule
import org.ems.CompaniesSystem
import spray.json._
import akka.util.Timeout
import org.ems.entities._
import akka.event.slf4j.SLF4JLogging
import org.ems.services.CompanyService
import scala.concurrent.duration._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import DefaultJsonProtocol._
trait Companies extends RouteConcatenation with SLF4JLogging {
  this: AkkaCoreModule =>

  import CompanySer._
  val companyActor: ActorRef =
    actorSystem.actorOf(Props[CompaniesSystem], "company")
  val companyService = new CompanyService(companyActor)
  implicit val timeout = Timeout(5 seconds)
  val companyRoute: Route = {
    pathPrefix("companies") {
      get {
        complete(
          companyService
            .getAllCompanies()
            .map(v => v)
        )
      }
    } ~ pathPrefix("company") {
      path(IntNumber) { id =>
        get {
          complete(companyService.getCompany(id).map(v => v))
        } ~
        delete {
          complete(companyService.deleteCompany(id).map(v => v.toString))
        }
      } ~
      post {
        entity(as[InsertCompany]) { comp: InsertCompany =>
          complete(companyService.addCompany(comp).map(_.toString()))
        }
      } ~
      put {
        entity(as[Company]) { comp: Company =>
          complete(companyService.updateCompany(comp).map(_.toString()))
        }
      }
    }
  }
}
