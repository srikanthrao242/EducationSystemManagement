/**/
package com.ems.utilities.companies.config

case class DBConfig(url: String,
                    driver: String,
                    username: String,
                    password: String,
                    connectTimeout: String,
                    poolSize: Int)
case class CompConfig(db:DBConfig)
case class CompanyConfig(`education-management-system`:CompConfig)