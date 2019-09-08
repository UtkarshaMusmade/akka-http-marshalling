import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{as, complete, concat, entity, get, path, post}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.{Json, OWrites, Reads}

import scala.io.StdIn
case class Person(name:String,age:Int)

object Person{
  implicit val toJson: OWrites[Person] = Json.writes[Person]
  implicit val fromJson: Reads[Person] = Json.reads[Person]

}
object PlayMarshalling extends PlayJsonSupport{
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("MainHighLevelAPI")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    lazy val routes: Route = {
      path("person") {
        concat(
          post {
            entity(as[Person]) { person =>
              complete(person)
            }
          },
          get{
            complete(Person("abc",21))
          }
        )
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



