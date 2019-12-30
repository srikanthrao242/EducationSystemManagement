/**/
package org.ems.services

import akka.actor.ActorRef

import scala.concurrent.ExecutionContext

class SalaryServices (salary: ActorRef)(implicit val ec: ExecutionContext)
  extends ActorMsgProcess(salary) {}