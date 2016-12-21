package example
package controllers

import scala.concurrent.Future
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax._

import models._
import data._

trait UserController {
  def findById(id: Int): Eff[Stack, User]
  def update(id: Int, user: User): Eff[Stack, User]
}

class UserControllerProd(userData: UserData) extends UserController {

  private[this] def updateEff[R: _result : _async: _log](id: Int, user: User): Eff[R, User] = for {
    _     <- tell(LogEntry(s"I'm the controller updating user $id"))
    _     <- userData.update(id, user)
    user  <- userData.findById(id)
  } yield user

  private[this] def findByIdEff[R: _result : _async: _log](id: Int): Eff[R, User] = for {
    _    <- tell(LogEntry(s"I'm the controller finding user $id"))
    user <- userData.findById(id)
  } yield user

  override def findById(id: Int): Eff[Stack, User] = findByIdEff(id)

  override def update(id: Int, user: User): Eff[Stack, User] = updateEff(id, user)

}
