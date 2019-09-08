import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import org.json4s._
import org.json4s.native.Serialization.{write => jWrite}

import scala.io.StdIn

case class Student(rollNum: Int, name: String)
object Student{

  implicit val formats: Formats = DefaultFormats

  def write[T <: AnyRef](value: T): String = jWrite(value)


}
object Json4smarshalling {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("MainHighLevelAPI")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    lazy val routes: Route =
      path("student") {
        get{
          complete(Student.write(Student(2,"xyz")))
        }
      }~
      post {
        path("addstudent") {
          entity(as[String]) { studentJson =>
            complete (studentJson)

          }
        }
      }


    val bindingFuture = Http().bindAndHandle(routes, "localhost", 8081)
    println(s"Server online at http://localhost:8081/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
