package dev.insideyou.playground.infrastructure.ui

import cats.data.Kleisli
import dev.insideyou.playground.infrastructure.controller.MessageWebController.{
  mainRoute,
  messageRoutes,
  webApplicationEnvironment
}
import org.http4s._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._

object WebUI {

  val webUIProgram: ZIO[ZEnv, Throwable, Unit] = ZIO
    .runtime[ZEnv]
    .flatMap { implicit rts =>
      BlazeServerBuilder[Task](rts.platform.executor.asEC)
        .bindHttp(8080, "localhost")
        .withHttpApp(mainApp)
        .serve
        .compile
        .drain
    }
    .provideSomeLayer[ZEnv](webApplicationEnvironment)

  private val mainApp: Kleisli[Task, Request[Task], Response[Task]] = Router[Task](
    mappings = ("/", mainRoute),
    ("/messages", messageRoutes)
  ).orNotFound
}
