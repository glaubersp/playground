package dev.insideyou
package playground

import zio._

import dev.insideyou.playground.infrastructure.ui.ConsoleUI.consoleUIExecution

object Main extends App {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (args.headOption match {
      case Some("console") => consoleUIExecution
      case Some(_)         => ZIO.succeed(2)
      case None            => ZIO.succeed(1)
    }).exitCode
}
