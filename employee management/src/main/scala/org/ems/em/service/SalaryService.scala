/**/
package org.ems.em.service

import cats.Show
import cats.effect.IO
import org.ems.em.database.DBUtil.Dao
import org.ems.em.database.DBUtil.Dao.Aux
import org.ems.em.database.DbModule
import org.ems.em.entities.Salary
import doobie._
import doobie.implicits._

trait SalaryService {
  implicit val salDao: Dao.Aux[Salary, Int] =
    Dao.derive[Salary, Int]("salary", "id")
  implicit val salShow: Show[Salary] = Show.fromToString
  val salDn: Aux[Salary, Int] = Dao[Salary]
  import salDn._

  def getEmployeeSalary(id: Int): IO[Option[Salary]] = DbModule.transactor.use {
    xa =>
      find(id).transact(xa)
  }
  def getAllSalaries: IO[List[Salary]] = DbModule.transactor.use { xa =>
    findAll.transact(xa).compile.toList
  }
  def updateSalary(salary: Salary): IO[Int] = DbModule.transactor.use { xa =>
    salary.id.fold {
      throw new Exception(s"Need id for salary update")
    } { id =>
      update(id, salary.copy(id = None)).transact(xa)
    }
  }
  def deleteSalary(id: Int): IO[Int] = DbModule.transactor.use { xa =>
    delete(id).transact(xa)
  }

}
