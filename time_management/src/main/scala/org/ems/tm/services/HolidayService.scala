/**/
package org.ems.tm.services
import cats.Show
import cats.effect.IO
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.time_mgt.etities.Holidays
import doobie._
import doobie.implicits._
import org.ems.tm.database.DbModule

trait HolidayService {
  implicit val holDao: Dao.Aux[Holidays, Int] =
    Dao.derive[Holidays, Int]("holidays", "id")
  implicit val holShow: Show[Holidays] = Show.fromToString
  val holDn = Dao[Holidays]
  import holDn._

  def getHolidays(db:String, id: Int): IO[Option[Holidays]] = DbModule.transactor.use {
    xa =>
      find(id,db).transact(xa)
  }
  def addHoliday(db:String, holidays: Holidays): IO[Int] = DbModule.transactor.use { xa =>
    insert(holidays,db).transact(xa)
  }
  def updateHolidays(db:String, holidays: Holidays): IO[Int] = DbModule.transactor.use {
    xa =>
      holidays.id.fold {
        throw new Exception(s"Need id to update the Holiday list")
      } { id =>
        update(id, holidays.copy(id = None),db).transact(xa)
      }
  }


}
