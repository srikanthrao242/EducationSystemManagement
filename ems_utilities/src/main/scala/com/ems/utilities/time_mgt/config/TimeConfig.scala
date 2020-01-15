/**/
package com.ems.utilities.time_mgt.config

case class DBConfig(url: String,
                    driver: String,
                    username: String,
                    password: String,
                    connectTimeout: String,
                    poolSize: Int)
case class Constants(db_prefix:String)
case class TimeConf(db:DBConfig, constants: Constants)
case class TimeConfig(`education-management-system`:TimeConf)
