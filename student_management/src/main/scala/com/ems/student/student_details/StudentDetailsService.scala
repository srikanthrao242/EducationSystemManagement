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

  def getStudentList(req: ListStudentRequest,
                     userDB: String): Future[List[StudentDetails]] = {
    val query =
      s"""
         |select  *
         |from $userDB.student_details as A
         |join
         |${StudentUtils.getDB(userDB,
                               req.academicID,
                               req.classID,
                               req.sectionID)}.students as B
         |on A.StudentID = B.StudentID;
         |""".stripMargin
    DbModule.transactor.use { xa =>
      Query0[StudentDetails](query).stream.transact(xa).compile.toList
    }.unsafeToFuture()

  }

  def addStudentDetails(student: StudentDetails, db: String): Future[Int] =
    DbModule.transactor.use { xa =>
      insert(student, db).transact(xa)
    }.unsafeToFuture()

  def deleteStudent(id: Int, db: String): Future[Int] =
    DbModule.transactor.use { xa =>
      delete(id, db).transact(xa)
    }.unsafeToFuture()

  def getStudentImage(id:Int,db:String): Future[Option[String]] ={
    val query =
      s"""
         |select `ProfileImage` from `$db`.`student_details`
         |where `StudentID`=$id
         |""".stripMargin
     DbModule.transactor.use{xa=>
        Query0[String](query).option.transact(xa)
      }.unsafeToFuture()

  }

}
