package dev.insideyou.playground.infrastructure.ui

import cats.data.Kleisli
import dev.insideyou.playground.domain.model.MessageRepository.MessageRepository
import dev.insideyou.playground.domain.service.MessageService
import dev.insideyou.playground.domain.service.MessageService.ComposeMessageRequest
import dev.insideyou.playground.infrastructure.environments.InMemoryMessageRepositoryEnv
import io.circe.generic.auto._
import org.http4s._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._

object WebUI {
  type WebApplicationEnvironment = MessageRepository
  val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  val webApplicationEnvironment: ZLayer[Any, Throwable, WebApplicationEnvironment] =
    InMemoryMessageRepositoryEnv.live

  val webProgram: ZIO[ZEnv, Throwable, Unit] = {
    val server: ZIO[zio.ZEnv, Throwable, Unit] = for {
      server <- ZIO
        .runtime[ZEnv]
        .flatMap { implicit rts =>
          BlazeServerBuilder[Task](rts.platform.executor.asEC)
            .bindHttp(8080, "localhost")
            .withHttpApp(mainApp)
            .serve
            .compile
            .drain
        }
    } yield server
    server.provideSomeLayer[ZEnv](webApplicationEnvironment)
  }

  private val mainRoute: HttpRoutes[Task] = HttpRoutes.of {
    case GET -> Root => Ok("Funciona!")
  }
  private val messageRoutes: HttpRoutes[Task] = HttpRoutes
    .of[Task] {
      case GET -> Root / messageId =>
        MessageService
          .getMessage(messageId)
          .flatMap {
            case Some(value) => Ok(value)
            case None        => NoContent()
          }
          .provideLayer(webApplicationEnvironment)
      case GET -> Root =>
        MessageService
          .getAllMessages
          .foldM(x => RIO.dieMessage(x.getMessage), messages => Ok(messages))
          .provideLayer(webApplicationEnvironment)
      case req @ POST -> Root =>
        req
          .decode[ComposeMessageRequest] { messageRequest =>
            MessageService
              .storeMessage(messageRequest)
              .foldM(x => RIO.dieMessage(x.getMessage), message => Ok(message))
              .provideLayer(webApplicationEnvironment)
          }
    }
  private val mainApp: Kleisli[Task, Request[Task], Response[Task]] = Router[Task](
    mappings = ("/", mainRoute),
    ("/messages", messageRoutes)
  ).orNotFound
}
