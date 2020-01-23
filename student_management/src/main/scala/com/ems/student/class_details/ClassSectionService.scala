/**/
package com.ems.student.class_details

import cats.Show
import cats.effect.IO
import com.ems.student.database.DbModule
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.student.entities._
import doobie._
import doobie.implicits._

import scala.concurrent.Future
trait ClassSectionService {

  implicit val classSectionDetailsDao: Dao.Aux[ClassSections, Int] =
    Dao.derive[ClassSections, Int]("class_sections", "SectionID")
  implicit val classSectionShow: Show[ClassSections] = Show.fromToString
  val classSectionDn: Aux[ClassSections, Int] = Dao[ClassSections]
  import classSectionDn._

  def addClassSection(`class`: ClassSections, db: String): Future[Int] =
    DbModule.transactor.use { xa =>
      insert(`class`, db).transact(xa)
    }.unsafeToFuture()


  def getAllClassSections(classID:Int,db:String): Future[List[ClassSections]] ={
    DbModule.transactor.use{xa=>
      findAllBy(classID, "ClassID", db).transact(xa).compile.toList
    }.unsafeToFuture()
  }

}
