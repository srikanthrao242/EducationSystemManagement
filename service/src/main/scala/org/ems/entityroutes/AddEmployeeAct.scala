/**/
package org.ems.entityroutes

import akka.actor.{Actor, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.ems.em.entities.AddEmployee
import akka.http.scaladsl.server.Directives._
import org.ems.em.service.InsertEmployee
import spray.json.DefaultJsonProtocol._

class AddEmployeeAct extends Actor with SprayJsonSupport{
  override def receive: Receive = {
    case (empId:Int, salId:Int, bankId:Int)=>
      complete((empId,salId,bankId))
    case emp: AddEmployee =>
      val ref = context.actorOf(Props(new InsertEmployee(self)), "InsertEmp")
      ref !  emp
  }
}
