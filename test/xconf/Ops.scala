package xconf

import play.api.libs.ws.WS
import scala.concurrent.Future
import play.api.http.Status
import Status.OK
import play.api.libs.json.Json
import concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Ops extends App {

  val urls = Seq("http://www.google.com", "http://mit.edu", "http://yahoo.com")

  val responses = urls.map { url =>
    WS.url(url).get()
  }

  val fResponses = Future.traverse(urls) { url =>
    WS
    .url(url)
    .get()
    .filter(_.status == OK)
    .map(_.body.lines.size)
    .recover {
      case ex =>
        println(ex.getMessage)
        0
    }
  }

  fResponses.onComplete {
    case Success(xs) => xs foreach(println)
    case Failure(ex) => println(ex.getMessage)
  }

}
