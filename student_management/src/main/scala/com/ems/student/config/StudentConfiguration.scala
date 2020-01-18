/**/
package com.ems.student.config

import com.ems.utilities.student.config.StudentConfig
import pureconfig.generic.ProductHint
import pureconfig.{CamelCase, ConfigFieldMapping, ConfigSource}
import pureconfig.generic.auto._

object StudentConfiguration {
  implicit def productHint[T]: ProductHint[T] =
    ProductHint(ConfigFieldMapping(CamelCase, CamelCase))
  val config = ConfigSource.default.load[StudentConfig] match {
    case Right(c) => c.`education-management-system`
    case Left(e) =>
      throw new RuntimeException("Config error: " + e.head)
  }
}
