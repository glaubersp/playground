package dev.insideyou.playground.infrastructure.http

import io.circe.Decoder
import org.http4s.client.Client
import zio._

import dev.insideyou.playground.domain.model.error.WebError

object HttpClient {
  type HttpClient = Has[HttpClient.Service]

  def http4s: URLayer[Has[Client[Task]], Has[Service]] =
    ZLayer.fromService[Client[Task], Service] { http4sClient: Client[Task] =>
      Http4s(http4sClient)
    }

  trait Service {
    def get[T](uri: String)(implicit d: Decoder[T]): IO[WebError, T]
  }
}
