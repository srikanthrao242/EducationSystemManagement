/**/
package org.ems.em.service

import akka.actor.{Actor, Props}
import akka.event.slf4j.SLF4JLogging
import com.ems.utilities.employees.entities.AddEmployee

class AddEmployeeActor extends Actor with SLF4JLogging{

  val ref = context.actorOf(Props(new InsertEmployee(self)), "InsertEmp")
  override def receive: Receive = {
    case emp: AddEmployee =>
      log.debug("Got message to AddEmployeeAct to add ")
      ref ! (sender(),emp)
  }

}
