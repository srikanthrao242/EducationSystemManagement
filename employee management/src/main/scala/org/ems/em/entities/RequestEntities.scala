/**/
package org.ems.em.entities

case object GetAllEmployeesProfile
case class GetEmployeeProfile(id:Int)
case class AddEmployeeProfile(employee: Employee)
case class UpdateEmployeeProfile(employee: Employee)
case class DeleteEmployeeProfile(id:Int)

case object GetAllSalaries
case class GetEmployeeSalary(salaryId:Int)
case class UpdateSalary(salary: Salary)
case class DeleteSalary(id:Int)
case class AddEmployeeSalary(salary: Salary)

case object GetAllBankDetails
case class GetBankDetails(id:Int)
case class UpdateBankDetails(bankDetails: BankDetails)
case class DeleteBankDetails(id:Int)
case class AddEmployeeBankDetails(bankDetails: BankDetails)

object RequestEntities {

}
