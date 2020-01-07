/**/
package org.ems.tm.config

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
case class TimeConf(db:DBConfig, constants: Constants)
case class TimeConfig(`education-management-system`:TimeConf)

object TimeConfig {
  implicit def productHint[T]: ProductHint[T] =
    ProductHint(ConfigFieldMapping(CamelCase, CamelCase))
  val config = ConfigSource.default.load[TimeConfig] match {
    case Right(c) => c.`education-management-system`
    case Left(e) =>
      throw new RuntimeException("Config error: " + e.head)
  }
}
