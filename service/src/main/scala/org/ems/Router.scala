/**/
package org.ems

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import akka.stream.scaladsl.FileIO
import org.ems.cm.CompanyRoutes
import org.ems.config.ESMConfig
import org.ems.em.routes._
import org.ems.entities._
import org.ems.entities.RegisterUserSer._
import org.ems.entityroutes.Register
import org.ems.um.UserRoutes
import org.ems.util.{CORSHandler, Util}
import org.ems.util.ExceptionHandling._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.util.{Failure, Success}
import com.ems.student.academic.AcademicsRoute
import com.ems.student.adminssion.NewStudent
import com.ems.student.class_details.ClassesRoute
import com.ems.student.student_details.StudentDetailsRoute
trait Router
  extends RouteConcatenation
  with CompanyRoutes
  with UserRoutes
  with Register
  with EmployeeRoutes
  with SalaryRoutes
  with BankRoutes
  with AcademicsRoute
  with ClassesRoute
  with NewStudent
  with StudentDetailsRoute
  with CORSHandler {
  this: AkkaCoreModule =>
  val client = ESMConfig.config.client
  val mainRoute: Route =
    corsHandler {
      handleExceptions(exceptionHandler) {
        handleRejections(rejectionHandler) {
          pathSingleSlash {
            complete(StatusCodes.OK)
          } ~ pathPrefix("api") {
            path("company-logo") {
              post {
                uploadFile("companylogos")
              }
            } ~ path("user-profile") {
              post {
                uploadFile("userimages")
              }
            } ~ path("employee-profile") {
              post {
                uploadFile("employeeimages")
              }
            } ~ path("student-profile") {
              post {
                uploadFile("studentimages")
              }
            } ~
            companyRoute ~
            registerRoute ~
            userRoute ~
            employeesRoute ~
            salaryRoute ~
            bankRoute ~
            academicRoute ~
            classesRoute ~
            newStudent ~
            studentDetailsRoute
          }
        }
      }
    }

  def uploadFile(dir: String): Route = fileUpload("image") {
    case (fileInfo, fileStream) =>
      val destFile = Util.getDestinationFile
      val sink = FileIO.toPath(Util.getDestPath(destFile, fileInfo, dir))
      val writeResult = fileStream.runWith(sink)
      onSuccess(writeResult) { result =>
        result.status match {
          case Success(_) =>
            log.debug(s"file created ${destFile + fileInfo.fileName}")
            complete(UploadImageRes(destFile + fileInfo.fileName))
          case Failure(e) =>
            log.error(s"Got error while copying file ", e)
            throw e
        }
      }
  }
}
