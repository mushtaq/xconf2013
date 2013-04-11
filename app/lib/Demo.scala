package lib

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import concurrent.ExecutionContext.Implicits.global

class Demo(count: Int) {

  private val addresses = io.Source.fromFile("conf/addresses.txt").getLines().to[Vector].take(count)
  private val addressService = new AddressService

  def sequential = addresses.map(a => getTitleWithLog(a, addressService.getTitleBlocking))
  def parallel = addresses.par.map(a => getTitleWithLog(a, addressService.getTitleBlocking))
  def async = await(Future.traverse(addresses)(a => getTitleFutureWithLog(a, addressService.getTitleAsync)))
  def nonBlocking = await(Future.traverse(addresses)(a => getTitleFutureWithLog(a, addressService.getTitleNonBlocking)))

  def time(block: => Unit) = {
    val begin = System.currentTimeMillis()
    block
    val end = System.currentTimeMillis()
    val total = (end - begin) / 1000
    println("-----------------------------------")
    println(s"Total time taken is: $total seconds")
  }

  private def await[T](value: Future[T]) = Await.result(value, 100.seconds)

  private def getTitleWithLog(address: String, getTitle: String => String) = {
    println(s"request: Getting title for: $address")
    val title = getTitle(address)
    println(s"RESPONSE: Title for $address is: ${title.trim.lines.next()}")
    title
  }

  private def getTitleFutureWithLog(address: String, getTitle: String => Future[String]) = {
    println(s"request: Getting title for: $address")
    val titleFuture = getTitle(address)
    titleFuture.foreach { title =>
      println(s"RESPONSE: Title for $address is: ${title.trim.lines.next()}")
    }
    titleFuture
  }
}
