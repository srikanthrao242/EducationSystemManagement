/**/
package com.ems.utilities.users.config

case class DBConfig(url: String,
                    driver: String,
                    username: String,
                    password: String,
                    connectTimeout: String,
                    poolSize: Int)
case class Constants(db_prefix: String)
case class UsConfig(db:DBConfig,constants: Constants)
case class UserConfig(`education-management-system`:UsConfig)
