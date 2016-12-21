package example
package models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class LogEntry(message: String) {
  import org.backuity.ansi.AnsiFormatter.FormattedHelper
  override def toString =
    ansi"[%yellow{${LocalDateTime.now.format(LogEntry.dateFormatter)}}] %bold{${message}}"
}

object LogEntry {
  private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
}
