/**/
package com.ems.student.class_details

import com.ems.utilities.student.entities.ClassCreateRequest

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

}
