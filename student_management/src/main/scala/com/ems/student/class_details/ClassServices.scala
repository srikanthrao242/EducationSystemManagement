/**/
package com.ems.student.class_details

import com.ems.utilities.student.entities.{ClassCreateRequest, ClassSectionDataSource}

import scala.concurrent.{ExecutionContext, Future}

trait ClassServices extends ClassDetailsService with ClassSectionService {

  implicit val executor: ExecutionContext
  def createClassesAndSections(createRequest: List[ClassCreateRequest],
                               db: String): Future[List[(Int, List[Int])]] =
    Future.traverse(createRequest)(req => {
      for {
        classId <- addClasses(req.classes, db)
        sectionId <- Future.traverse(req.sections)(
          sections =>
            addClassSection(sections.copy(ClassID = Some(classId)), db)
        )
      } yield {
        (classId, sectionId)
      }
    })


  def getClassesAndSections(academicID:Int,db: String): Future[List[ClassSectionDataSource]] ={
    for{
      classes <- getClasses(academicID,db)
      resp <- Future.traverse(classes)(cls=>
        for{
          sections <- getAllClassSections(cls.ClassID.get, db)
        }yield {
          ClassSectionDataSource(cls.ClassName,cls.NumberOfSections, cls.Fee, cls.FeeType, sections)
        }
      )
    }yield resp
  }

}
