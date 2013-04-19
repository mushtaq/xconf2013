package services

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class BulkTitleService {

  val titleService = new TitleService

   def getTitlesNonBlocking(count: Int): Future[Vector[String]] = {
     val addresses = groupedAddresses.next()

     Future.traverse(addresses) { address =>
       titleService.getTitleNonBlocking(address)
     }
   }

   def getTitlesBlocking(count: Int): Vector[String] = {
     val addresses = groupedAddresses.next()

     addresses.map { address =>
       titleService.getTitleBlocking(address)
     }
   }

   def getTitlesAsync(count: Int): Future[Vector[String]] = {
     val addresses = groupedAddresses.next()

     Future.traverse(addresses) { address =>
       titleService.getTitleAsync(address)
     }
   }


   private lazy val allAddresses = io.Source.fromFile("conf/addresses.txt").getLines().to[Vector]
   private lazy val groupedAddresses = allAddresses.grouped(3)

 //  private def getRandomAddresses(count: Int) = allAddresses.grouped(count)
 }
