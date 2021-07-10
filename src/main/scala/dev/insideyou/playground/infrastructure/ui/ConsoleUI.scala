package dev.insideyou.playground.infrastructure.ui

import zio._
import zio.console._

import dev.insideyou.playground.domain.model.MessageRepository
import dev.insideyou.playground.domain.model.MessageRepository.MessageRepository
import dev.insideyou.playground.infrastructure.controller.CRUDOperation._
import dev.insideyou.playground.infrastructure.controller._
import dev.insideyou.playground.infrastructure.environments.MessageRepositoryEnv

object ConsoleUI {
  type ConsoleApplicationEnvironment = Console with MessageRepository

  val consoleApplicationEnvironment
      : ZLayer[Any, Nothing, Console with Has[MessageRepository.Service]] =
    Console.live ++ MessageRepositoryEnv.inMemory

  val consoleUIExecution: IO[Nothing, Int] =
    consoleProgram().provideLayer(consoleApplicationEnvironment)

  def consoleProgram(): ZIO[ConsoleApplicationEnvironment, Nothing, Int] =
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
                .flatMap(s => putStrLn(s"Succeeded with $s") *> consoleProgram())
                .orElse(consoleProgram())
          }
        case None =>
          putStrLn(s"$selection is not a valid selection, please try again!") *> consoleProgram()
      }
    } yield 0).tapError(e => putStrLn(s"Unexpected Failure $e")) orElse ZIO.succeed(1)

  private def dispatch(operation: CRUDOperation): ZIO[Console with MessageRepository, Any, Any] =
    operation match {
      case Create  => MessageController.create
      case Read    => MessageController.read
      case Update  => MessageController.update
      case Delete  => MessageController.delete
      case GetAll  => MessageController.getAll
      case ExitApp => ZIO.fail("How did I get here?")
    }
}
