/**/
package org.ems.em.entities

import akka.actor.ActorRef

case class GetAllEmployeesProfile(userId: Int)
case class GetEmployeeProfile(userId: Int, id: Int)
case class AddEmployeeProfile(userId: Int, employee: Employee)
case class UpdateEmployeeProfile(userId: Int, employee: Employee)
case class DeleteEmployeeProfile(userId: Int, id: Int)

case class GetAllSalaries(userId: Int)
case class GetEmployeeSalary(userId: Int, salaryId: Int)
case class UpdateSalary(userId: Int, salary: Salary)
case class DeleteSalary(userId: Int, id: Int)
case class AddEmployeeSalary(userId: Int, salary: Salary)

case class GetAllBankDetails(userId: Int)
case class GetBankDetails(userId: Int, id: Int)
case class UpdateBankDetails(userId: Int, bankDetails: BankDetails)
case class DeleteBankDetails(userId: Int, id: Int)
case class AddEmployeeBankDetails(userId: Int, bankDetails: BankDetails)

case class MessageBack(empId:Int, salId:Int, bankId:Int)

case class AddEmployee(userId: Int, employee:Employee, salary: Salary, bankDetails: BankDetails)



case class GetSalaryD(ref:ActorRef,db: String, employee: List[Employee])
case class GetBankD(ref:ActorRef,db: String, employee: List[Employee], salary: List[Salary])
case class EmpSalBank(ref:ActorRef,employee: List[Employee], salary: List[Salary], bankDetails: List[BankDetails])
case class EmpSalBankR(employee: List[Employee], salary: List[Salary], bankDetails: List[BankDetails])

object AckingReceiver {
  case object Ack

  case object StreamInitialized
  case object StreamCompleted
  final case class StreamFailure(ex: Throwable)
}

object RequestEntities {

}
