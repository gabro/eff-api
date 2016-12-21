package example
package data

import slick.jdbc.H2Profile.api._

import models.User

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey)
  def name = column[String]("name")
  def * = (id, name) <> (User.tupled, User.unapply)
}

object Users extends TableQuery[Users](new Users(_))
