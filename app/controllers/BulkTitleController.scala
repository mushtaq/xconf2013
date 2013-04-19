package controllers

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.BulkTitleService

object BulkTitleController extends Controller {

  private val bulkTitleService = new BulkTitleService

  def getTitlesNonBlocking(count: Int) = Action {
    Async {
      val titlesFuture = bulkTitleService.getTitlesNonBlocking(count)
      titlesFuture.map { titles => Ok(titles.mkString("\n")) }
    }
  }

  def getTitlesBlocking(count: Int) = Action {
    val titles = bulkTitleService.getTitlesBlocking(count: Int)
    Ok(titles.mkString("\n"))
  }

  def getTitlesAsync(count: Int) = Action {
    Async {
      val titlesFuture = bulkTitleService.getTitlesAsync(count)
      titlesFuture.map { titles => Ok(titles.mkString("\n")) }
    }
  }
}
