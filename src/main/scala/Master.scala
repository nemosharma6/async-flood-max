import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class Master extends Actor with ActorConfig with ActorLogging {

  var counter = 0
  var proc1: ActorRef = _
  var proc2: ActorRef = _
  var proc3: ActorRef = _

  override def receive: Receive = {
    case FindMeAWorthyLeader =>
      val p1 = system.actorOf(Process.props(1, null, 3), name = "p1")
      val p2 = system.actorOf(Process.props(2, null, 3), name = "p2")
      val p3 = system.actorOf(Process.props(3, null, 3), name = "p3")

      p1 ! Neighbors(List(p2, p3))
      p2 ! Neighbors(List(p1))
      p3 ! Neighbors(List(p1))

      proc1 = p1
      proc2 = p2
      proc3 = p3

      p1 ! Initiate
      p2 ! Initiate
      p3 ! Initiate

    case Leader(src, mx) =>
      log.info(s"$src returned id = $mx")

      counter += 1
      if (counter == 3) {
        log.info("Time for Master to die")
        context.system.terminate()
      }
  }
}

object Master {
  def props: Props = Props[Master]
}
