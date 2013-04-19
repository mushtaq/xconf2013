package controllers

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.TitleService

object TitleController extends Controller {

  private val titleService = new TitleService

  def getTitleNonBlocking(address: String) = Action {

    Async {
      val titleFuture = titleService.getTitleNonBlocking(address)
      titleFuture.map { title =>
        Ok(title)
      }
    }
  }

  def getTitleBlocking(address: String) = Action {

    val title = titleService.getTitleBlocking(address)
    Ok(title)
  }

  def getTitleAsync(address: String) = Action {

    Async {
      val titleFuture = titleService.getTitleAsync(address)
      titleFuture.map { title =>
        Ok(title)
      }
    }
  }
}
