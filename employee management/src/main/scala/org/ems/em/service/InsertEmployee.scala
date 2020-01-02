/**/
package org.ems.em.service

import akka.actor.SupervisorStrategy.Escalate
import akka.actor.{Actor, ActorRef, OneForOneStrategy}
import akka.event.slf4j.SLF4JLogging
import org.ems.em.config.EmployeeConfig
import org.ems.em.entities._

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class InsertEmployee(ref: ActorRef)
  extends Actor
  with SalaryService
  with BankServices
  with EmployeeService
  with SLF4JLogging {

  implicit val ec = context.system.dispatcher
  val retries = 5
  override val supervisorStrategy: OneForOneStrategy =
    OneForOneStrategy(maxNrOfRetries = retries, withinTimeRange = 1 minute) {
      case _: Exception => Escalate
    }

  case class AddSalBank(id: Int, sal: Salary, bank: BankDetails)
  case class AddBank(id: Int, salId: Int, bank: BankDetails)

  override def receive: Receive = {
    case AddEmployee(userId, emp, sal, bank) =>
      addEmployeeProfile(getDB(userId), emp).unsafeToFuture().map { eid =>
        self ! AddSalBank(userId,
                          sal.copy(employeeId = Some(eid)),
                          bank.copy(employeeId = Some(eid)))
      }
    case AddSalBank(userId, sal, bank) =>
      addSalary(getDB(userId), sal).unsafeToFuture() andThen {
        case Success(id) =>
          self ! AddBank(userId, id, bank)
        case Failure(ex) =>
          for {
            _ <- deleteEmployeeProfile(getDB(userId), sal.employeeId.get)
              .unsafeToFuture()
          } yield { throw ex}
      }
    case AddBank(userId, salId, bank) =>
      addEmployeeBankDetails(getDB(userId), bank).unsafeToFuture() andThen {
        case Success(id) =>
          ref ! MessageBack(bank.employeeId.get, salId, id)
        case Failure(ex) =>
          for {
            _ <- deleteEmployeeProfile(getDB(userId), bank.employeeId.get)
              .unsafeToFuture()
            _ <- deleteSalary(getDB(userId), salId).unsafeToFuture()
          } yield { throw ex}
      }
  }

  def getDB(userId: Int): String =
    s"${EmployeeConfig.config.constants.db_prefix}_$userId"
}
