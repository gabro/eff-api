package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._
import slick.jdbc.H2Profile.api._

import models._
import controllers._
import data._
import EffectStackMarshaller._
import ResultMarshaller._

object Boot extends App {

  implicit val system = ActorSystem("http-router")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val db = Database.forConfig("h2mem")

  val userData = new UserDataProd(db)
  val userController = new UserControllerProd(userData)

  val route = pathPrefix("users") {
    (get & path(IntNumber)) { id =>
      complete(userController.findById(id))
    } ~
    (put & path(IntNumber) & entity(as[User])) { (id, user) =>
      complete(userController.update(id, user))
    }
  }

  // Create the db and some users
  val setup = DBIO.seq(
    Users.schema.create,
    Users ++= Seq(
      User(1, "Gabro"),
      User(2, "Dani")
    )
  )

  db.run(setup)

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  println(s"Server online at http://localhost:8080\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete { _ =>
      db.run(DBIO.seq(Users.schema.drop))
      system.terminate()
    }

}
