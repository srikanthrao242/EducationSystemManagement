/**/
package com.ems.student.academic

import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives._
import com.ems.student.database.DbModule._
import com.ems.utilities.student.entities.AcademicSer._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import com.ems.utilities.student.entities.Academic
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext

trait AcademicsRoute extends SLF4JLogging with AcademicsServices {
  implicit val executor: ExecutionContext

  val academicRoute: Route = pathPrefix("academics" / IntNumber) { userId =>
    path("all-academics") {
      get {
        log.debug(s"got request for all-academics")
        complete(findAllAcademics(getDB(userId)))
      }
    } ~ path("new-academic") {
      post {
        entity(as[Academic]) { ac =>
          log.debug(s"got request for add new academic $ac")
          complete(addNew(getDB(userId), ac).map(v => v.toString))
        }
      }
    } ~ path("activity" / IntNumber / Segment) { (id, ac) =>
      log.debug(s"got request to change activity of $id as $ac")
      complete("")
    }
  }

}
