package lib

import scala.concurrent.{Await, Future}
import scala.collection.GenSeq
import scala.concurrent.duration._
import concurrent.ExecutionContext.Implicits.global

class Demo(count: Int) {

  private val addresses = io.Source.fromFile("conf/addresses.txt").getLines().to[Vector].take(count)
  private val addressService = new AddressService

  def sequential = addresses.map(addressService.getTitleBlocking)
  def parallel = addresses.par.map(addressService.getTitleBlocking)
  def async = Future.traverse(addresses)(addressService.getTitleAsync)
  def nonBlocking = Future.traverse(addresses)(addressService.getTitleNonBlocking)

  def time(titles: => GenSeq[String]) = {
    val begin = System.currentTimeMillis()
    titles.foreach(t => println(s"Title: ${t.trim.lines.next()}"))
    val end = System.currentTimeMillis()
    val total = (end - begin) / 1000
    println("-----------------------------------")
    println(s"Total time taken is: $total seconds")
  }

  def await[T](value: Future[T]) = Await.result(value, 100.seconds)
}
