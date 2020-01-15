/**/
package org.ems.em.service

import cats.Show
import cats.effect.IO
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.database.DBUtil._
import org.ems.em.database.DbModule
import com.ems.utilities.employees.entities.Salary
import doobie._
import doobie.implicits._

trait SalaryService {
  implicit val salDao: Dao.Aux[Salary, Int] =
    Dao.derive[Salary, Int]("salary", "id")
  implicit val salShow: Show[Salary] = Show.fromToString
  val salDn: Aux[Salary, Int] = Dao[Salary]
  import salDn._

  def getEmployeeSalary(db: String, id: Int): IO[Option[Salary]] =
    DbModule.transactor.use { xa =>
      find( id,db).transact(xa)
    }
  def getAllSalaries(db: String): IO[List[Salary]] = DbModule.transactor.use {
    xa =>
      findAll(db).transact(xa).compile.toList
  }
  def updateSalary(db: String, salary: Salary): IO[Int] =
    DbModule.transactor.use { xa =>
      salary.id.fold {
        throw new Exception(s"Need id for salary update")
      } { id =>
        update( id, salary,db).transact(xa)
      }
    }
  def deleteSalary(db: String, id: Int): IO[Int] = DbModule.transactor.use {
    xa =>
      delete( id,db).transact(xa)
  }
  def addSalary(db: String, salary: Salary): IO[Int] = DbModule.transactor.use {
    xa =>
      insert( salary,db).transact(xa)
  }

  def getSalaryByEmpId(db: String, empId: Int): IO[Option[Salary]] =
    DbModule.transactor.use { xa =>
      findBy( empId, "employeeId",db).transact(xa)
    }

}
