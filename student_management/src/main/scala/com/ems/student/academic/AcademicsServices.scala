/**/
package com.ems.student.academic

import cats.Show
import com.ems.student.database.DbModule
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.student.entities.Academic
import doobie._
import doobie.implicits._

import scala.concurrent.Future
trait AcademicsServices {
  implicit val academicDao: Dao.Aux[Academic, Int] =
    Dao.derive[Academic, Int]("academic_details", "AcademicID")
  implicit val academicShow: Show[Academic] = Show.fromToString
  val academicDn: Aux[Academic, Int] = Dao[Academic]
  import academicDn._

  def addNew(db: String, academic: Academic): Future[Int] =
    DbModule.transactor.use { xa =>
      insert(academic, db).transact(xa)
    }.unsafeToFuture()

  def findAllAcademics(db: String): Future[List[Academic]] =
    DbModule.transactor.use { xa =>
      findAll(db).transact(xa).compile.toList
    }.unsafeToFuture()

  def findAllAcademicNames(db: String): Future[List[String]] = {
    val query =
      s"""
         |SELECT AcademicName FROM $db.academic_details
         |""".stripMargin
    DbModule.transactor.use { xa =>
      Query0[String](query).stream.transact(xa).compile.toList
    }.unsafeToFuture()
  }

  def makeInactive(db: String, id: Int): Unit = {
    val query =
      s"""
         |
         |""".stripMargin
  }

}
