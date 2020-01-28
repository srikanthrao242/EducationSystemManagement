/**/
package com.ems.utilities

import spray.json._
import DefaultJsonProtocol._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object Mappable {

  implicit object AnyJsonFormat extends JsonFormat[Any] {
    def write(x: Any) = x match {
      case n: Int => JsNumber(n)
      case n: BigDecimal => JsNumber(n)
      case s: String => JsString(s)
      case b: Boolean if b => JsTrue
      case b: Boolean if !b => JsFalse
    }

    def read(value: JsValue) = value match {
      case JsNumber(n) => n
      case JsString(s) => s
      case JsTrue => true
      case JsFalse => false
    }
  }

  def toMap[T: RootJsonFormat](t: T): Map[String, Any] = {
    t.toJson.convertTo[Map[String, Any]]
  }

  def fromMap[T: RootJsonFormat](map: Map[String, Any])(implicit executor: ExecutionContext): Future[T] = Future {
    map.toJson.convertTo[T]
  }
}
