/**/
package org.ems.em.service

import cats.Show
import cats.effect.IO
import com.ems.utilities.employees.entities._
import org.ems.em.database.DbModule
import doobie._
import doobie.implicits._
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.database.DBUtil._

import scala.concurrent.Future

trait EmployeeService {
  implicit val empDao: Dao.Aux[Employee, Int] =
    Dao.derive[Employee, Int]("employees", "id")
  implicit val empShow: Show[Employee] = Show.fromToString
  val empDn: Aux[Employee, Int] = Dao[Employee]
  import empDn._

  def getAllEmployeesProfiles(db: String): IO[List[Employee]] =
    DbModule.transactor.use { xa =>
      findAll(db).transact(xa).compile.toList
    }

  def getEmployeeProfile(db: String, id: Int): IO[Option[Employee]] =
    DbModule.transactor.use { xa =>
      find( id,db).transact(xa)
    }

  def deleteEmployeeProfile(db: String, id: Int): IO[Int] =
    DbModule.transactor.use { xa =>
      delete( id,db).transact(xa)
    }

  def updateEmployeeProfile(db: String, employee: Employee): IO[Int] =
    DbModule.transactor.use { xa =>
      employee.id
        .fold(throw new Exception("Employee Id is mandatory to update"))(id => {
          update(id, employee,db).transact(xa)
        })
    }

  def addEmployeeProfile(db: String, employee: Employee): IO[Int] =
    DbModule.transactor.use { xa =>
      insert( employee,db).transact(xa)
    }

  def getEmployeeProImage(db: String, id: Int): Future[Option[String]] = DbModule.transactor.use { xa =>
    Query[Int, String](s"""
                SELECT employeeProfile
                FROM $db.employees
                WHERE id = ?
              """).option(id).transact(xa)
  }.unsafeToFuture()

}
