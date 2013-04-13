package controllers

import play.api.mvc._
import concurrent.ExecutionContext.Implicits.global
import lib.{BulkTitleService, TitleService}
import play.api.Logger

object BulkTitleController extends Controller {

  private val bulkTitleService = new BulkTitleService

  def getTitles(count: Int) = Action {
    Async {
      val titlesFuture = bulkTitleService.getTitles(count)
      titlesFuture.map { titles => Ok(titles.mkString("\n")) }
    }
  }

  def getTitlesBlocking(count: Int) = Action {
    val titles = bulkTitleService.getTitlesBlocking(count: Int)
    Ok(titles.mkString("\n"))
  }
}
