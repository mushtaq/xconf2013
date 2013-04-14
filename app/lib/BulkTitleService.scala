package lib

import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class BulkTitleService {

  def getTitlesFuture(count: Int): Future[Vector[String]] = {
    val addresses = getRandomAddresses(count)

    Future.traverse(addresses) { address =>
      titleService.getTitleFuture(address)
    }
  }

  def getTitles(count: Int): Vector[String] = {
    val addresses = getRandomAddresses(count)

    addresses.map { address =>
      titleService.getTitle(address)
    }
  }

  private lazy val titleService = new TitleService
  private lazy val allAddresses = io.Source.fromFile("conf/addresses.txt").getLines().to[Vector].take(500)

  private def getRandomAddresses(count: Int) = Random.shuffle(allAddresses).take(count)
}
