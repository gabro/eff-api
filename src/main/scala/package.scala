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
  type RequestIdReader[A] = Reader[RequestId, A]

  type Stack = Fx.fx4[LogWriter, Async, Result, RequestIdReader]

  type _log[R] = LogWriter |= R
  type _async[R] = Async |= R
  type _result[R] = Result |= R
  type _requestId[R] = RequestIdReader |= R

  val futureInterpreter = {
    import scala.concurrent.ExecutionContext.Implicits.global
    AsyncFutureInterpreter.create
  }
  import futureInterpreter._

  def runEffect[A](eff: Eff[Stack, A]): Future[Result[A]] =
    eff
      .runReader(RequestId.generate)
      .runEither
      .runWriter
      .runAsyncFuture.map { case (result, logs) =>
        logs.foreach(println)
        result
      }

  def log[R: _log: _requestId](message: String): Eff[R, RequestId] = for {
    requestId <- ask
    _         <- tell(LogEntry(message, Some(requestId)))
  } yield requestId

}
