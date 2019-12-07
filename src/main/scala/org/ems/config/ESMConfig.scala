package org.ems.config

import pureconfig.{CamelCase, ConfigFieldMapping, ConfigSource}
import pureconfig.generic.ProductHint


case class HttpConfig(host:String, port: Int)
case class EMS(http: HttpConfig)
case class EmsConf(ems: EMS)

object ESMConfig {
  implicit def productHint[T]: ProductHint[T] = ProductHint(ConfigFieldMapping(CamelCase, CamelCase))
  val config = ConfigSource.default.load[EmsConf] match {
    case Right(c) => c.ems
    case Left(e) =>
      throw new RuntimeException("Config error: " + e)
  }
}
