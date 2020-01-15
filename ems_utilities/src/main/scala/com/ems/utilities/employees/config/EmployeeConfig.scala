/**/
package com.ems.utilities.employees.config

case class DBConfig(url: String,
                    driver: String,
                    username: String,
                    password: String,
                    connectTimeout: String,
                    poolSize: Int)
case class Constants(db_prefix:String)
case class EmpConfig(db:DBConfig, constants: Constants)
case class EmployeeConfig(`education-management-system`:EmpConfig)

