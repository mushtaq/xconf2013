
import concurrent._
import ExecutionContext.Implicits.global

println("asdasd")
future(1)

val xs = Seq(1,2,3)

val results = Future.traverse(xs) { x =>
  future(x)
}


results.onComplete(println)


