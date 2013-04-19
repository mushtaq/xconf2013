package lib

import concurrent._
import ExecutionContext.Implicits.global
import Utils._

class IOSimulator(sleepTime: Int) {

  def blockingLoop(count: Int) = await(
    loop(count, Thread.sleep(_))
  )

  def nonBlockingLoop(count: Int) = await(
    loop(count, Timer.delay(_))
  )

  private def loop(count: Int, delayer: Int => Unit) =
    Future.traverse((1 to count).toList) { n =>
      delayedResult(n.toString, delayer)
    }

  private def delayedResult(result: String, delayer: Int => Unit) = future {
    delayer(sleepTime)
    println(s"finished item: $result")
    result
  }
}
