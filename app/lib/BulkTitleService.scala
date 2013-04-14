package lib

import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class BulkTitleService {

  def getTitles(count: Int) = Future.traverse(getRandomAddreses(count))(titleService.getTitle)
  def getTitlesBlocking(count: Int) = getRandomAddreses(count).map(titleService.getTitleBlocking)

  private lazy val titleService = new TitleService
  private lazy val addresses = io.Source.fromFile("conf/addresses.txt").getLines().to[Vector].take(500)
  private def getRandomAddreses(count: Int) = Random.shuffle(addresses).take(count)
}
