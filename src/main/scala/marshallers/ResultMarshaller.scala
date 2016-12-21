package example

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshalling._
import PredefinedToResponseMarshallers.fromToEntityMarshaller
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._

trait ResultMarshaller {

  implicit def resultMarshaller[A: ToEntityMarshaller](implicit m: ToEntityMarshaller[LogicError]): ToResponseMarshaller[Result[A]] =
    Marshaller { implicit ec =>
      {
        case Right(a) => fromToEntityMarshaller[A]().apply(a)
        case Left(e @ LogicError.NotFound(_)) => fromToEntityMarshaller[LogicError](StatusCodes.NotFound).apply(e)
        case Left(e) => fromToEntityMarshaller[LogicError](StatusCodes.BadRequest).apply(e)
      }
    }
}

object ResultMarshaller extends ResultMarshaller
