/**/
package org.ems.entityroutes
import akka.actor._
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import org.ems.AkkaCoreModule
import org.ems.CompaniesSystem
import spray.json._
import DefaultJsonProtocol._
import akka.util.Timeout
import org.ems.entities.{Company, GetAll, GetAllCompanies}

import scala.concurrent.duration._
trait Companies extends RouteConcatenation{
  this: AkkaCoreModule=>

  implicit val companySer = jsonFormat8(Company)
  val companyActor: ActorRef = actorSystem.actorOf(Props[CompaniesSystem], "company")

  implicit val timeout = Timeout(5 seconds)
  val companyRoute : Route ={
    pathPrefix("companies"){
      get{
        companyActor ? GetAllCompanies
        complete("OK")
      }
    }

  }

}
