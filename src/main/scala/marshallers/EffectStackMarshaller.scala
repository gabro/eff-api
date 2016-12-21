package example

import akka.http.scaladsl.marshalling._
import org.atnos.eff.Eff
import scala.concurrent.Future

trait EffectStackMarshaller {
  implicit def effectStackMarshaller[A, B](implicit
    m: Marshaller[Future[Result[A]], B]
  ): Marshaller[Eff[Stack, A], B] = Marshaller { implicit ec => eff => m(runEffect(eff)) }
}

object EffectStackMarshaller extends EffectStackMarshaller
