package com.ems.utilities.student.entities

object StudentUtils {

  def getDB(academicYear: String, employeeId: String): String = {
    s"ems_${academicYear}_$employeeId"
  }

}
