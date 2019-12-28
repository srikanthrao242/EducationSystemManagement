/*
 */
package org.ems.config

import pureconfig._
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._
case class HttpConfig(host: String, port: Int)
case class DBConfig(url: String,
                    driver: String,
                    username: String,
                    password: String,
                    connectTimeout: String,
                    poolSize: Int)
case class ClientHttp(host: String, port: Int)
case class EMS(http: HttpConfig, client: ClientHttp)
case class EmsConf(`education-management-system`: EMS)

object ESMConfig {
  implicit def productHint[T]: ProductHint[T] =
    ProductHint(ConfigFieldMapping(CamelCase, CamelCase))
  val config = ConfigSource.default.load[EmsConf] match {
    case Right(c) => c.`education-management-system`
    case Left(e) =>
      throw new RuntimeException("Config error: " + e.head)
  }
}
