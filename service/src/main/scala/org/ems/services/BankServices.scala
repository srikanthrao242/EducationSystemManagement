/**/
package org.ems.services

import akka.actor.ActorRef

import scala.concurrent.ExecutionContext

class BankServices (bank: ActorRef)(implicit val ec: ExecutionContext)
  extends ActorMsgProcess(bank) {}

