/**/
package com.ems.student.parent_details

import cats.Show
import cats.effect.IO
import com.ems.student.database.DbModule
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.student.entities._
import doobie._
import doobie.implicits._

import scala.concurrent.Future
trait ParentDetailsService {
  implicit val parentDetailsDao: Dao.Aux[ParentDetails, Int] =
    Dao.derive[ParentDetails, Int]("parent_details", "ID")
  implicit val parentDetailsShow: Show[ParentDetails] = Show.fromToString
  val parentDetailsDn: Aux[ParentDetails, Int] = Dao[ParentDetails]
  import parentDetailsDn._

  def addParentDetails(parent: ParentDetails, db: String): Future[Int] =
    DbModule.transactor.use { xa =>
      insert(parent, db).transact(xa)
    }.unsafeToFuture()

  def deleteParentDetails(id:Int,db:String):Future[Int] ={
    DbModule.transactor.use{xa=>
      delete(id,db).transact(xa)
    }.unsafeToFuture()
  }

}
