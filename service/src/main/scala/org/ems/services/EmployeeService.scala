/**/
package org.ems.services

import akka.actor.ActorRef
import scala.concurrent.ExecutionContext

class EmployeeService(employee: ActorRef)(implicit val ec: ExecutionContext)
  extends ActorMsgProcess(employee) {}
