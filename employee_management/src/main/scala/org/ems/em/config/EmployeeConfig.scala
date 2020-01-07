/**/
package org.ems.em.config
import pureconfig.generic.ProductHint
import pureconfig.{CamelCase, ConfigFieldMapping, ConfigSource}
import pureconfig.generic.auto._

case class DBConfig(url: String,
                    driver: String,
                    username: String,
                    password: String,
                    connectTimeout: String,
                    poolSize: Int)
case class Constants(db_prefix:String)
case class EmpConfig(db:DBConfig, constants: Constants)
case class EmployeeConfig(`education-management-system`:EmpConfig)

object EmployeeConfig {
  implicit def productHint[T]: ProductHint[T] =
    ProductHint(ConfigFieldMapping(CamelCase, CamelCase))
  val config = ConfigSource.default.load[EmployeeConfig] match {
    case Right(c) => c.`education-management-system`
    case Left(e) =>
      throw new RuntimeException("Config error: " + e.head)
  }
}

