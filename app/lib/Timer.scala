package lib

import scala.concurrent._
import concurrent.duration._
import ExecutionContext.Implicits.global
import Utils._

object Timer {

  private val p = promise[Int]

  def delay(timeInMillis: Int): Unit = await(
    delayedFuture(timeInMillis)
  )

  private def delayedFuture(timeInMillis: Int) = future {
    Await.result(p.future, timeInMillis.milliseconds)
  }.recover {
    case ex => ()
  }
}
