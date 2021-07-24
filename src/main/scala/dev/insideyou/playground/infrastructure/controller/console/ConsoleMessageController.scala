package dev.insideyou.playground.infrastructure.controller.console

import zio._
import zio.console._

import dev.insideyou.playground.domain.model.Message
import dev.insideyou.playground.domain.service.MessageService
import dev.insideyou.playground.domain.service.MessageService._
import dev.insideyou.playground.infrastructure.persistence.MessageRepository

object ConsoleMessageController {
  type ConsoleApplicationEnvironment = Console with Has[MessageRepository]
  val consoleApplicationEnvironment: ZLayer[Any, Nothing, Console with Has[MessageRepository]] =
    Console.live ++ MessageRepository.live

  val create: RIO[ConsoleApplicationEnvironment, Message] =
    for {
      _ <- putStrLn("Please type your message:")
      message <- getStrLn
      storedMessage <- MessageService.storeMessage(ComposeMessageRequest(message))
    } yield storedMessage

  val read: RIO[ConsoleApplicationEnvironment, Option[Message]] =
    for {
      _ <- putStrLn("Please insert id of Message you want to read:")
      id <- getStrLn
      message <- MessageService.getMessage(id)
    } yield message

  val update: RIO[ConsoleApplicationEnvironment, Message] =
    for {
      _ <- putStrLn("Please insert Message id:")
      id <- getStrLn
      _ <- putStrLn("Please insert Message new content:")
      content <- getStrLn
      updatedMessage <- MessageService.updateMessage(UpdateMessageRequest(id, content))
    } yield updatedMessage

  val delete: RIO[ConsoleApplicationEnvironment, Option[Message]] =
    for {
      _ <- putStrLn("Please insert id of Message you want to delete:")
      id <- getStrLn
      deletedMessage <- MessageService.deleteMessage(id)
    } yield deletedMessage

  val getAll: RIO[ConsoleApplicationEnvironment, Seq[Message]] =
    MessageService.getAllMessages

}
