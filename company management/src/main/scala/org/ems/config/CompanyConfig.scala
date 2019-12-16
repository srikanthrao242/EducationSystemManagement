/**/
package org.ems.config

import pureconfig.generic.ProductHint
import pureconfig.{CamelCase, ConfigFieldMapping, ConfigSource}
import pureconfig.generic.auto._

case class DBConfig(url: String,
                    driver: String,
                    username: String,
                    password: String,
                    connectTimeout: String,
                    poolSize: Int)
case class CompConfig(db:DBConfig)
case class CompanyConfig(`education-management-system`:CompConfig)

object CompanyConfig {
  implicit def productHint[T]: ProductHint[T] =
    ProductHint(ConfigFieldMapping(CamelCase, CamelCase))
  val config = ConfigSource.default.load[CompanyConfig] match {
    case Right(c) => c.`education-management-system`
    case Left(e) =>
      throw new RuntimeException("Config error: " + e.head)
  }
}
