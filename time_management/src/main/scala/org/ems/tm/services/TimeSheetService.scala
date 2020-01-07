/**/
package org.ems.tm.services
import cats.Show
import cats.effect.IO
import doobie._
import doobie.implicits._
import org.ems.tm.database.DBUtil.Dao
import org.ems.tm.database.DbModule
import org.ems.tm.etities.TimeSheet


trait TimeSheetService {
  implicit val tsDao: Dao.Aux[TimeSheet, Int] =
    Dao.derive[TimeSheet, Int]("time_sheet", "id")
  implicit val tsShow: Show[TimeSheet] = Show.fromToString
  val tsDn = Dao[TimeSheet]
  import tsDn._

  def getTimeSheet(db:String, id: Int): IO[Option[TimeSheet]] = DbModule.transactor.use {
    xa =>
      find(db,id).transact(xa)
  }

  def addTimeSheet(db:String, timeSheet: TimeSheet): IO[Int] = DbModule.transactor.use {
    xa =>
      insert(db,timeSheet).transact(xa)
  }

  def updateTimeSheet(db:String, timeSheet: TimeSheet): IO[Int] = DbModule.transactor.use {
    xa =>
      timeSheet.id.fold {
        throw new Exception(s"new timesheet id to update")
      } { id =>
        update(db,id, timeSheet.copy(id = None)).transact(xa)
      }

  }
}
