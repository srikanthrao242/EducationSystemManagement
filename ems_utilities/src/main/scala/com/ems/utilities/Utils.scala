package com.ems.utilities

import java.sql.Timestamp

import spray.json.{DeserializationException, JsString, JsValue, RootJsonFormat}

object Utils {

  implicit object DateJsonFormat extends RootJsonFormat[Timestamp] {
    override def write(obj: Timestamp): JsString = JsString(obj.toString)
    override def read(json: JsValue) : Timestamp = json match {
      case JsString(s) => Timestamp.valueOf(s)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }

}
