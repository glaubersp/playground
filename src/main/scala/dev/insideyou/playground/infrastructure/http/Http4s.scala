package dev.insideyou.playground.infrastructure.http

import io.circe._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.{ EntityDecoder, _ }
import zio._
import zio.interop.catz._

import dev.insideyou.playground.domain.model.error.WebError
import dev.insideyou.playground.domain.model.error.WebError._

final case class Http4s(client: Client[Task]) extends HttpClient.Service {
  implicit def entityDecoder[A](implicit decoder: Decoder[A]): EntityDecoder[Task, A] =
    jsonOf[Task, A]
  implicit def entityEncoder[A](implicit encoder: Encoder[A]): EntityEncoder[Task, A] =
    jsonEncoderOf[Task, A]

  def get[T](uri: String)(implicit d: Decoder[T]): IO[WebError, T] = {
    def call(uri: Uri): IO[WebError, T] =
      client
        .expect[T](uri)
        .foldM(_ => IO.fail(NotFound(uri.renderString)), result => ZIO.succeed(result))

    Uri
      .fromString(uri)
      .fold(_ => IO.fail(MalformedUrl(uri)), call)
  }
}
