package dev.insideyou
package playground

import dev.insideyou.playground.infrastructure.ui.ConsoleUI.consoleUIExecution
import dev.insideyou.playground.infrastructure.ui.WebUI.webProgram
import zio._

object Main extends App {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (args.headOption match {
      case Some("console") => consoleUIExecution
      case Some("web")     => webProgram
      case _               => ZIO.succeed(1)
    }).exitCode
}
