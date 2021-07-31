package dev.insideyou
package playground

import zio._
import dev.insideyou.playground.infrastructure.controller.console.ConsoleMessageController.consoleApplicationEnvironment
import dev.insideyou.playground.infrastructure.controller.console.ConsoleUI.consoleUIProgram
import dev.insideyou.playground.infrastructure.controller.web.WebMessageController.webApplicationEnvironment
import dev.insideyou.playground.infrastructure.controller.web.WebUI.webUIProgram

object Main extends App {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (args.headOption match {
      case Some("console") => consoleUIProgram.provideLayer(consoleApplicationEnvironment)
      case Some("web")     => webUIProgram.provideCustomLayer(webApplicationEnvironment)
      case _               => ZIO.succeed(1)
    }).exitCode
}
