/**/
package com.ems.student.examinations

import akka.event.slf4j.SLF4JLogging

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import com.ems.student.database.DbModule
import com.ems.utilities.examination.{Examinations_Details, Examinations_Subjects}
import com.ems.utilities.examination.ExaminationsSer._
import spray.json.DefaultJsonProtocol._
trait ExaminationRoute
  extends SLF4JLogging
  with ExaminationsService
  with ExamSubjectServices {
  implicit val executor: ExecutionContext
  val examinationRoute: Route = pathPrefix("examination" / IntNumber) {
    userID =>
    path("exams"/IntNumber){ academicId=>
      get{
        log.debug(s"got request to get all exams of academic $academicId")
        complete{
          getExams(academicId, DbModule.getDB(userID))
        }
      }
    }~
      path("add-exam") {
        post {
          entity(as[Examinations_Details]) { exam =>
            log.debug(s"Got request to add examination details $exam")
            complete {
              addExam(exam, DbModule.getDB(userID)).map(v => v.toString)
            }
          }
        }
      } ~ path("exam-subject" / IntNumber) { examId =>
        log.debug(s"Got request to get the exam-subjects")
        complete {
          getExamSubjectByExamID(examId, DbModule.getDB(userID))
        }
      } ~ path("delete-exam-subject" / IntNumber) { examId =>
        delete{
          log.debug(s"Got request to delete the exam-subjects")
          complete {
            deleteSubject(examId, DbModule.getDB(userID)).map(v=>v.toString)
          }
        }
      }~ path("add-exam-sub") {
        post {
          entity(as[Examinations_Subjects]) { sub =>
            log.debug(s"Got request to add the exam-subjects $sub")
            complete {
              addSubject(sub, DbModule.getDB(userID)).map(v=> v.toString)
            }
          }
        }
      }
  }
}
