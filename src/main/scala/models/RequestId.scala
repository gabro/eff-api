package example
package models

import java.util.UUID

case class RequestId(value: UUID) extends AnyVal

object RequestId {
  def generate = RequestId(UUID.randomUUID)
}
