package example
package data

import org.atnos.eff._
import org.atnos.eff.all._
import futureInterpreter.fromFuture
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

import models._

trait UserData {
  def findById[R: _result : _async : _log](id: Int): Eff[R, User]
  def update[R: _result :_async: _log](id: Int, user: User): Eff[R, Int]
}

class UserDataProd(db: Database) extends UserData {

  def findById[R: _result : _async : _log](id: Int): Eff[R, User] = for {
    maybeUser <- fromFuture(db.run(Users.filter(_.id === id).result.headOption))
    user      <- optionEither(maybeUser, LogicError.notFound(s"user with id $id not found"))
  } yield user

  def update[R: _result :_async: _log](id: Int, user: User): Eff[R, Int] = for {
    numberOfUpdates <- fromFuture(db.run(Users.filter(_.id === id).update(user)))
  } yield numberOfUpdates

}
