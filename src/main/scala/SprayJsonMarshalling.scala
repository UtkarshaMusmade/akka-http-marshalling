import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, path, post,concat}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import spray.json.RootJsonFormat
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import scala.io.StdIn

case class User(name: String, age: Int)

object User{
  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat2(User.apply)
}

object SprayJsonMarshalling {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("MainHighLevelAPI")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    lazy val routes: Route =
      path("user") {
        concat(
          get {
            complete(
              User(name = "abc", age = 21)
            )
          },
          post{
            entity(as[User]){user =>
              complete(StatusCodes.Created,user)
            }
          })
      }
    val bindingFuture = Http().bindAndHandle(routes, "localhost", 8084)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}


