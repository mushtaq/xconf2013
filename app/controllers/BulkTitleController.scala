package controllers

import play.api.mvc._
import concurrent.ExecutionContext.Implicits.global
import services.BulkTitleService

object BulkTitleController extends Controller {

  private val bulkTitleService = new BulkTitleService

  def getTitlesNonBlocking(count: Int) = Action {
    Async {
      val titlesFuture = bulkTitleService.getTitlesFuture(count)
      titlesFuture.map { titles => Ok(titles.mkString("\n")) }
    }
  }

  def getTitles(count: Int) = Action {
    val titles = bulkTitleService.getTitles(count: Int)
    Ok(titles.mkString("\n"))
  }
}
