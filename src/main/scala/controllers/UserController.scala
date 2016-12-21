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

  private[this] def findByIdEff[R: _result : _async: _log : _requestId](id: Int): Eff[R, User] = for {
    _    <- log(s"UserController: finding user with id $id")
    user <- userData.findById(id)
  } yield user

  private[this] def updateEff[R: _result : _async: _log : _requestId](id: Int, user: User): Eff[R, User] = for {
    _    <- log(s"UserController: updating user with id $id")
    _     <- userData.update(id, user)
    user  <- userData.findById(id)
  } yield user

  override def findById(id: Int): Eff[Stack, User] = findByIdEff(id)

  override def update(id: Int, user: User): Eff[Stack, User] = updateEff(id, user)

}
