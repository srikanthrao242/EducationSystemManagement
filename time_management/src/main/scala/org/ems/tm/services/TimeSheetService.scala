/**/
package org.ems.tm.services
import cats.Show
import cats.effect.IO
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.time_mgt.etities.TimeSheet
import doobie._
import doobie.implicits._
import org.ems.tm.database.DbModule


trait TimeSheetService {
  implicit val tsDao: Dao.Aux[TimeSheet, Int] =
    Dao.derive[TimeSheet, Int]("time_sheet", "id")
  implicit val tsShow: Show[TimeSheet] = Show.fromToString
  val tsDn = Dao[TimeSheet]
  import tsDn._

  def getTimeSheet(db:String, id: Int): IO[Option[TimeSheet]] = DbModule.transactor.use {
    xa =>
      find(id,db).transact(xa)
  }

  def addTimeSheet(db:String, timeSheet: TimeSheet): IO[Int] = DbModule.transactor.use {
    xa =>
      insert(timeSheet,db).transact(xa)
  }

  def updateTimeSheet(db:String, timeSheet: TimeSheet): IO[Int] = DbModule.transactor.use {
    xa =>
      timeSheet.id.fold {
        throw new Exception(s"new timesheet id to update")
      } { id =>
        update(id, timeSheet.copy(id = None),db).transact(xa)
      }

  }
}
