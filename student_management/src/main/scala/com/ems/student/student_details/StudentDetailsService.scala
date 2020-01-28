/* */
package com.ems.student.student_details

import cats.Show
import cats.effect.IO
import com.ems.student.database.DbModule
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.student.entities._
import doobie._
import doobie.implicits._

import scala.concurrent.Future
trait StudentDetailsService {

  implicit val studentDetailsDao: Dao.Aux[StudentDetails, Int] =
    Dao.derive[StudentDetails, Int]("student_details", "StudentID")
  implicit val studentDetailsShow: Show[StudentDetails] = Show.fromToString
  val studentDetailsDn: Aux[StudentDetails, Int] = Dao[StudentDetails]
  import studentDetailsDn._


  def getStudentDetails(id: Int, db: String): Future[Option[StudentDetails]] =
    DbModule.transactor.use { xa =>
      find(id, db).transact(xa)
    }.unsafeToFuture()

  def addStudentDetails(student: StudentDetails, db: String): Future[Int] =
    DbModule.transactor.use { xa =>
      insert(student, db).transact(xa)
    }.unsafeToFuture()

  def deleteStudent(id:Int, db:String):Future[Int]={
    DbModule.transactor.use{xa=>
      delete(id,db).transact(xa)
    }.unsafeToFuture()
  }

}
