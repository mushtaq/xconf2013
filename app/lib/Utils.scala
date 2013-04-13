package lib

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Utils {

  def time(block: => Unit) = {
    val begin = System.currentTimeMillis()
    block
    val end = System.currentTimeMillis()
    val total = (end - begin) / 1000
    println("-----------------------------------")
    println(s"Total time taken is: $total seconds")
  }

  def await[T](value: Future[T]) = Await.result(value, 100.seconds)
}
