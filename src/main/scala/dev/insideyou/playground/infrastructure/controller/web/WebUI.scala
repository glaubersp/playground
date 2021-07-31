package dev.insideyou.playground.infrastructure.controller.web

import java.util.concurrent.Executors
import scala.concurrent._
import io.circe._
import org.http4s._
import org.http4s.circe._
import org.http4s.implicits._
import org.http4s.server.Router
import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._
import dev.insideyou.playground.infrastructure.controller.web.WebMessageController._
import org.http4s.blaze.server.BlazeServerBuilder

object WebUI {
  implicit val singleThreadContext: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(3))

  implicit def circeJsonDecoder[A](implicit decoder: Decoder[A]): EntityDecoder[Task, A] =
    jsonOf[Task, A]
  implicit def circeJsonEncoder[A](implicit decoder: Encoder[A]): EntityEncoder[Task, A] =
    jsonEncoderOf[Task, A]

  lazy val webUIProgram: ZIO[zio.ZEnv, Throwable, Unit] = ZIO
    .runtime[ZEnv]
    .flatMap { implicit rts =>
      BlazeServerBuilder
        .apply[Task](singleThreadContext)
        .bindHttp(8080, "localhost")
        .withHttpApp(mainApp)
        .serve
        .compile
        .drain
    }

  private val mainApp = Router[Task](
    mappings = ("/", mainRoute),
    ("/messages", messageRoutes)
  ).orNotFound
}
