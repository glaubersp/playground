package dev.insideyou
package playground

import dev.insideyou.playground.infrastructure.ui.ConsoleUI.consoleUIProgram
import dev.insideyou.playground.infrastructure.ui.WebUI.webUIProgram
import zio._

object Main extends App {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (args.headOption match {
      case Some("console") => consoleUIProgram
      case Some("web")     => webUIProgram
      case _               => ZIO.succeed(1)
    }).exitCode
}
