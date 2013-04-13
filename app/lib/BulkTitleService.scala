package lib

import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class BulkTitleService {

  private val titleService = new TitleService

  private val addresses = io.Source.fromFile("conf/addresses.txt").getLines().to[Vector].take(200)
  private def getRandomAddreses(count: Int) = Random.shuffle(addresses).take(count)

  def getTitles(count: Int) = Future.traverse(getRandomAddreses(count))(a => getTitleFutureWithLog(a, titleService.getTitle))
  def getTitlesBlocking(count: Int) = getRandomAddreses(count).map(a => getTitleWithLog(a, titleService.getTitleBlocking))

  def parallel = addresses.par.map(a => getTitleWithLog(a, titleService.getTitleBlocking))
  def async = Future.traverse(addresses)(a => getTitleFutureWithLog(a, titleService.getTitleAsync))

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
