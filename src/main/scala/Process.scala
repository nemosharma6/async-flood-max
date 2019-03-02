import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class Process(id: Int, var list: List[ActorRef], n: Int) extends Actor with ActorLogging {

  var max: Int = id
  var counter = 0
  var master: ActorRef = _

  override def receive: Receive = {

    case Initiate =>
      master = sender()
      log.info(s"master -> ${master.toString()}")
      list.foreach(_ ! FloodMessage(id, max))

    case FloodMessage(s, v) =>
      log.info(s"(Src, Msg) -> ($s, $v)")
      if (counter < n) {
        if (v > max) {
          max = v
        }

        list.foreach(_ ! FloodMessage(id, max))
        counter += 1

      } else {
        master ! Leader(id, max)
        log.info(s"Time for $id to die")
        context.system.terminate()
      }

      log.info(s"Current Leader for id $id is $max")

    case Neighbors(l) =>
      list = l
  }
}

object Process {
  def props(id: Int, list: List[ActorRef], n: Int): Props =
    Props(new Process(id, list, n))
}
