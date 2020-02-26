/**/
package com.ems.student.examinations

import akka.event.slf4j.SLF4JLogging

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import com.ems.student.database.DbModule
import com.ems.utilities.examination.Examinations_Details
import com.ems.utilities.examination.ExaminationsSer._

trait ExaminationRoute extends SLF4JLogging with ExaminationsService {
  implicit val executor: ExecutionContext
  val examinationRoute: Route = pathPrefix("examination" / IntNumber) {
    userID =>
      path("add-exam") {
        entity(as[Examinations_Details]) { exam =>
          log.debug(s"Got request to add examination details $exam")
          complete {
            addExam(exam, DbModule.getDB(userID)).map(v => v.toString)
          }
        }
      }
  }
}
