package com.ems.utilities.student.config

case class DBConfig(url: String,
                    driver: String,
                    username: String,
                    password: String,
                    connectTimeout: String,
                    poolSize: Int)
case class Constants(db_prefix:String)
case class StudConfig(db:DBConfig, constants: Constants)
case class StudentConfig(`education-management-system`:StudConfig)


