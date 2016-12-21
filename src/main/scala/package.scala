import cats._, data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import scala.concurrent.Future

package object example {

  import models._

  sealed trait LogicError
  object LogicError {
    case class NotFound(message: String) extends LogicError
    def notFound(message: String): LogicError = NotFound(message)
  }

  type LogWriter[A] = Writer[LogEntry, A]
  type Result[+A] = Either[LogicError, A]

  type Stack = Fx.fx3[LogWriter, Async, Result]

  type _log[R] = LogWriter |= R
  type _async[R] = Async |= R
  type _result[R] = Result |= R

  val futureInterpreter = {
    import scala.concurrent.ExecutionContext.Implicits.global
    AsyncFutureInterpreter.create
  }
  import futureInterpreter._

  def runEffect[A](eff: Eff[Stack, A]): Future[Result[A]] =
    eff.runEither.runWriter.runAsyncFuture.map { case (result, logs) =>
      logs.foreach(println)
      result
    }

}
