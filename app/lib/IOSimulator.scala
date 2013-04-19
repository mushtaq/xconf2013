package lib

import concurrent._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import Utils._

class IOSimulator(sleepTime: Int) {

  val fetchNewsBlocking = (t: Int) => Thread.sleep(t)
  val fetchNewsNonBlocking = (t: Int) => Timer.delay(t)

  def blockingIO(count: Int) = loop(count, Thread.sleep(_))

  def nonBlockingIO(count: Int) = loop(count, Timer.delay(_))

  def loop(count: Int, delayer: Int => Unit) =
    await(
      Future.traverse((1 to count).toList) { n =>
        delayedResult(n.toString, delayer)
      }
    )

  private def delayedResult(result: String, delayer: Int => Unit) = future {
    delayer(sleepTime)
    println(s"finished item: $result")
    result
  }
}
