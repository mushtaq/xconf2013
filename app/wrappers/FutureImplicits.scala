package wrappers

import scala.concurrent.Future

object FutureImplicits {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  implicit class RichFuture[T](val future: Future[T]) {
    def orElse[U >: T](that: => U) = future.recover { case _ => that }
    def orElseWith[U >: T](that: => Future[U]) = future.recoverWith { case _ => that }
  }

  implicit class RichFutureObject(val future: Future.type) {
    def collectSuccessful[A, B <: AnyRef](seq: Seq[A])(f: A => Future[B]): Future[Seq[B]] = {
      val sentinelValue = null.asInstanceOf[B]
      future.traverse(seq) { x =>
        f(x) recover { case _ => sentinelValue }
      } map { ys =>
        ys filterNot (_ == sentinelValue)
      }
    }
  }
}
