package dev.insideyou.playground.infrastructure.controller

import dev.insideyou.playground.domain.model.MessageRepository.MessageRepository
import dev.insideyou.playground.domain.service.MessageService
import dev.insideyou.playground.domain.service.MessageService.ComposeMessageRequest
import dev.insideyou.playground.infrastructure.environments.InMemoryMessageRepositoryEnv
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import zio.interop.catz._
import zio.interop.catz.implicits._
import zio.{ Task, ZLayer }

object MessageWebController {
  type WebApplicationEnvironment = MessageRepository

  val webApplicationEnvironment: ZLayer[Any, Throwable, WebApplicationEnvironment] =
    InMemoryMessageRepositoryEnv.live

  val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  val mainRoute: HttpRoutes[Task] = HttpRoutes.of {
    case GET -> Root => Ok("Web Interface is working!")
  }
  val messageRoutes: HttpRoutes[Task] = HttpRoutes
    .of[Task] {
      case GET -> Root / messageId =>
        MessageService
          .getMessage(messageId)
          .foldM(
            e => InternalServerError(e.toString),
            {
              case Some(value) => Ok(value)
              case None        => NoContent()
            }
          )
          .provideLayer(webApplicationEnvironment)
      case GET -> Root =>
        MessageService
          .getAllMessages
          .foldM(e => InternalServerError(e.toString), messages => Ok(messages))
          .provideLayer(webApplicationEnvironment)
      case req @ POST -> Root =>
        req
          .decode[ComposeMessageRequest] { messageRequest =>
            MessageService
              .storeMessage(messageRequest)
              .foldM(_ => NotModified(), message => Ok(message))
              .provideLayer(webApplicationEnvironment)
          }
    }
}
