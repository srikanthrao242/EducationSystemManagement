/**/
package org.ems.tm.services
import cats.Show
import cats.effect.IO
import doobie._
import doobie.implicits._
import org.ems.tm.database.DBUtil.Dao
import org.ems.tm.database.DbModule
import org.ems.tm.etities.Holidays

trait HolidayService {
  implicit val holDao: Dao.Aux[Holidays, Int] =
    Dao.derive[Holidays, Int]("holidays", "id")
  implicit val holShow: Show[Holidays] = Show.fromToString
  val holDn = Dao[Holidays]
  import holDn._

  def getHolidays(id: Int): IO[Option[Holidays]] = DbModule.transactor.use {
    xa =>
      find(id).transact(xa)
  }
  def addHoliday(holidays: Holidays): IO[Int] = DbModule.transactor.use { xa =>
    insert(holidays).transact(xa)
  }
  def updateHolidays(holidays: Holidays): IO[Int] = DbModule.transactor.use {
    xa =>
      holidays.id.fold {
        throw new Exception(s"Need id to update the Holiday list")
      } { id =>
        update(id, holidays.copy(id = None)).transact(xa)
      }
  }


}
