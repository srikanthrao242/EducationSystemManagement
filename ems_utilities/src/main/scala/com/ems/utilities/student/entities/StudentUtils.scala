package com.ems.utilities.student.entities

object StudentUtils {

  def getDB(db:String, academicId:Int, classId:Int, sectionId:Int): String = {
    s"${db}_${academicId}_${classId}_$sectionId"
  }

}
