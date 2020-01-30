/**/
package com.ems.student.adminssion

import akka.event.slf4j.SLF4JLogging
import com.ems.student.parent_details.ParentDetailsService
import com.ems.student.student_details.{AcademicStudentService, StudentDetailsService}
import com.ems.utilities.Mappable
import com.ems.utilities.student.entities._
import com.ems.utilities.student.entities.StudentSer._
import com.ems.student.admission_details.{AdmissionDetailsService, EducationQualificationService}
import com.ems.student.admission_fee_details.AdmissionFeeDetailsService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait NewAdmissionServices
  extends StudentDetailsService
  with ParentDetailsService
  with EducationQualificationService
  with AdmissionDetailsService
  with AdmissionFeeDetailsService
  with AcademicStudentService
  with SLF4JLogging {
  implicit val executor: ExecutionContext

  def recoverStudentParent(studentId: Int, parentID: Int, db: String): Unit =
    for {
      stuId <- deleteStudent(studentId, db)
      parId <- deleteParentDetails(parentID, db)
    } yield { log.debug(s"deleted student and parent $stuId, $parId") }

  def recoverStudentParentAdmin(studentId: Int,
                                parentID: Int,
                                admissionId: Int,
                                db: String): Unit =
    for {
      stuId <- deleteStudent(studentId, db)
      parId <- deleteParentDetails(parentID, db)
      admId <- deleteAdmission(admissionId, db)
    } yield {
      log.debug(s"deleted student, parent and admission $stuId, $parId, $admId")
    }

  def admitNewStudent(req: Map[String, Any], db: String): Future[Int] =
    for {
      studentDetails <- Mappable.fromMap[StudentDetails](req)
      studentId <- addStudentDetails(studentDetails, db)
      parentDetails <- Mappable.fromMap[ParentDetails](
        req + ("StudentID" -> studentId)
      )
      parentID <- addParentDetails(parentDetails, db) recover {
        case ex: Exception =>
          deleteStudent(studentId, db)
          throw ex
      }
      _ <- Mappable.fromMap[EducationQualification](
        req + ("StudentID" -> studentId)
      ) andThen {
        case Success(value) => addEdQual(value, db)
        case Failure(ex) =>
          recoverStudentParent(studentId, parentID, db)
          throw ex
      }
      (admissionId, admission) <- addAdmissionDetails(req,
                                                      db,
                                                      studentId,
                                                      parentID)
      _ <- addAcademicStudent(AcademicStudent(None, studentId),
                              StudentUtils.getDB(db,
                                                 admission.AcademicID,
                                                 admission.ClassID,
                                                 admission.SectionID)) recover {
        case ex: Exception =>
          recoverStudentParentAdmin(studentId, parentID, admissionId, db)
          throw ex
      }
    } yield {
      admissionId
    }

  def addAdmissionDetails(req: Map[String, Any],
                          db: String,
                          studentId: Int,
                          parentID: Int): Future[(Int, Admission)] =
    for {
      admission <- Mappable.fromMap[Admission](req + ("StudentID" -> studentId)) recover {
        case ex: Exception =>
          recoverStudentParent(studentId, parentID, db)
          throw ex
      }
      admissionId <- addNewAdmission(admission, db) recover {
        case ex: Exception =>
          recoverStudentParent(studentId, parentID, db)
          throw ex
      }
      admissionFee <- Mappable.fromMap[AdmissionFeeDetails](
        req + ("StudentID" -> studentId) + ("AdmissionID" -> admissionId)
      ) recover {
        case ex: Exception =>
          recoverStudentParentAdmin(studentId, parentID, admissionId, db)
          throw ex
      }
      _ <- addNewAdmissionFee(admissionFee, db) recover {
        case ex: Exception =>
          recoverStudentParentAdmin(studentId, parentID, admissionId, db)
          throw ex
      }
    } yield {
      (admissionId, admission)
    }

}
