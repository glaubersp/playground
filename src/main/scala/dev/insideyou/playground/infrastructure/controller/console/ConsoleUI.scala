package dev.insideyou.playground.infrastructure.controller.console

import zio.ZIO
import zio.console._
import dev.insideyou.playground.infrastructure.controller.console.CRUDOperation._
import dev.insideyou.playground.infrastructure.controller.console.ConsoleMessageController.ConsoleApplicationEnvironment
import dev.insideyou.playground.infrastructure.persistence.InMemoryMessageRepository

object ConsoleUI {
  lazy val consoleUIProgram: ZIO[zio.ZEnv, Nothing, Int] =
    consoleUIMenu.provideCustomLayer(InMemoryMessageRepository.live)

  val consoleUIMenu: ZIO[ConsoleApplicationEnvironment, Nothing, Int] =
    (for {
      _ <- putStrLn("Please select next operation to perform:")
      _ <- putStrLn(s"${Create.index} for Create") *> putStrLn(
        s"${Read.index} for Read"
      ) *> putStrLn(
        s"${Update.index} for Update"
      ) *> putStrLn(s"${Delete.index} for Delete") *> putStrLn(
        s"${GetAll.index} for GetAll"
      ) *> putStrLn(
        s"${ExitApp.index} for ExitApp"
      )
      selection <- getStrLn
      _ <- CRUDOperation.selectOperation(selection) match {
        case Some(op) =>
          op match {
            case ExitApp => putStrLn(s"Shutting down")
            case _ =>
              dispatch(op)
                .tapError(e => putStrLn(s"Failed with: $e"))
                .flatMap(s => putStrLn(s"Succeeded with $s") *> consoleUIMenu)
                .orElse(consoleUIMenu)
          }
        case None =>
          putStrLn(s"$selection is not a valid selection, please try again!") *> consoleUIMenu
      }
    } yield 0).tapError(e => putStrLn(s"Unexpected Failure $e")) orElse ZIO.succeed(1)

  private def dispatch(operation: CRUDOperation): ZIO[ConsoleApplicationEnvironment, Any, Any] =
    operation match {
      case Create  => ConsoleMessageController.create
      case Read    => ConsoleMessageController.read
      case Update  => ConsoleMessageController.update
      case Delete  => ConsoleMessageController.delete
      case GetAll  => ConsoleMessageController.getAll
      case ExitApp => ZIO.fail("How did I get here?")
    }
}
