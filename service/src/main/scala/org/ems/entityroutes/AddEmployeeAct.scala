/**/
package org.ems.entityroutes

import akka.actor.{Actor, ActorRef, Props}
import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.ems.em.entities.{AddEmployee, MessageBack}
import akka.http.scaladsl.server.Directives._
import org.ems.em.service.InsertEmployee
import spray.json.DefaultJsonProtocol._

class AddEmployeeAct extends Actor with SprayJsonSupport with SLF4JLogging {
  val ref = context.actorOf(Props(new InsertEmployee(self)), "InsertEmp")
  var original = Option.empty[ActorRef]
  override def receive: Receive = {
    case MessageBack(empId: Int, salId: Int, bankId: Int) =>
      log.debug(s"Got message back $empId, $salId, $bankId")
      original.fold(throw new Exception(s"No original Actor found")) { act =>
        act ! MessageBack(empId, salId, bankId)
      }
    case emp: AddEmployee =>
      log.debug("Got message to AddEmployeeAct to add ")
      original = Some(sender)
      ref ! emp
  }
}
