package example
package models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class LogEntry(message: String, requestId: Option[RequestId] = None) {
  import org.backuity.ansi.AnsiFormatter.FormattedHelper
  val id = "[" + requestId.map(_.value).getOrElse("") + "]"
  override def toString =
    ansi"[%yellow{${LocalDateTime.now.format(LogEntry.dateFormatter)}}] %blue{$id} %bold{${message}}"
}

object LogEntry {
  private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
}
