package com.ems.utilities

import java.sql.{Date, Timestamp}
import java.text.SimpleDateFormat
import java.util.Calendar

import spray.json.{DeserializationException, JsString, JsValue, RootJsonFormat}

object Utils {

  implicit object DateTimeJsonFormat extends RootJsonFormat[Timestamp] {
    override def write(obj: Timestamp): JsString = JsString(obj.toString)
    override def read(json: JsValue) : Timestamp = json match {
      case JsString(s) => Timestamp.valueOf(s)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }

  implicit object DateJsonFormat extends RootJsonFormat[Date] {
    override def write(obj: Date): JsString = JsString(obj.toString)
    override def read(json: JsValue) : Date = json match {
      case JsString(s) => Date.valueOf(s)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }

  def getCurrentDateTime(): Unit ={
    val today = Calendar.getInstance()
    val sdf = new SimpleDateFormat("YYYY")
  }

}
