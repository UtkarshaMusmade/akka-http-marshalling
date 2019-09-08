import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.fasterxml.jackson.databind.{ DeserializationFeature,ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.io.StdIn

case class Person(name: String, age: Int)

object Person{

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def toJson(value: Any)= {
    mapper.writeValueAsString(value)
  }


}
object JacksonMarshalling {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("MainHighLevelAPI")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    lazy val routes: Route = {
      path("person") {
        get {
            complete(Person.toJson(Person("abc", 21)))
            }
      }
    }


    val bindingFuture = Http().bindAndHandle(routes, "localhost", 8083)
    println(s"Server online at http://localhost:8083/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
