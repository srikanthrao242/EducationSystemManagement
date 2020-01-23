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
trait ClassDetailsService {

  implicit val classDetailsDao: Dao.Aux[Classes, Int] =
    Dao.derive[Classes, Int]("classes", "ClassID")
  implicit val classShow: Show[Classes] = Show.fromToString
  val classDn: Aux[Classes, Int] = Dao[Classes]
  import classDn._

  def addClasses(classes:Classes,db:String): Future[Int] ={
    DbModule.transactor.use{xa=>
      insert(classes,db).transact(xa)
    }.unsafeToFuture()
  }

  def getClasses(academicId:Int,db:String): Future[List[Classes]] ={
    DbModule.transactor.use{xa=>
      findAllBy(academicId, "AcademicID", db).transact(xa).compile.toList
    }.unsafeToFuture()
  }

}
