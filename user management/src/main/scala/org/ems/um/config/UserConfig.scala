/**/
package org.ems.um.config

import pureconfig.generic.ProductHint
import pureconfig.{CamelCase, ConfigFieldMapping, ConfigSource}
import pureconfig.generic.auto._

case class DBConfig(url: String,
                    driver: String,
                    username: String,
                    password: String,
                    connectTimeout: String,
                    poolSize: Int)
case class UsConfig(db:DBConfig)
case class UserConfig(`education-management-system`:UsConfig)

object UserConfig {
  implicit def productHint[T]: ProductHint[T] =
    ProductHint(ConfigFieldMapping(CamelCase, CamelCase))
  val config = ConfigSource.default.load[UserConfig] match {
    case Right(c) => c.`education-management-system`
    case Left(e) =>
      throw new RuntimeException("Config error: " + e.head)
  }
}
