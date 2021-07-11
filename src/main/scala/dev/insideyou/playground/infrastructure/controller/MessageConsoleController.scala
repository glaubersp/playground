package dev.insideyou.playground.infrastructure.controller

import zio._
import zio.console._

import dev.insideyou.playground.domain.model.Message
import dev.insideyou.playground.domain.model.MessageRepository.MessageRepository
import dev.insideyou.playground.domain.service.MessageService
import dev.insideyou.playground.domain.service.MessageService._

object MessageConsoleController {
  val create: RIO[Console with MessageRepository, Message] =
    for {
      _ <- putStrLn("Please type your message:")
      message <- getStrLn
      storedMessage <- MessageService.storeMessage(ComposeMessageRequest(message))
    } yield storedMessage

  val read: RIO[Console with MessageRepository, Option[Message]] =
    for {
      _ <- putStrLn("Please insert id of Message you want to read:")
      id <- getStrLn
      message <- MessageService.getMessage(id)
    } yield message

  val update: RIO[Console with MessageRepository, Message] =
    for {
      _ <- putStrLn("Please insert Message id:")
      id <- getStrLn
      _ <- putStrLn("Please insert Message new content:")
      content <- getStrLn
      updatedMessage <- MessageService.updateMessage(UpdateMessageRequest(id, content))
    } yield updatedMessage

  val delete: RIO[Console with MessageRepository, Option[Message]] =
    for {
      _ <- putStrLn("Please insert id of Message you want to delete:")
      id <- getStrLn
      deletedMessage <- MessageService.deleteMessage(id)
    } yield deletedMessage

  val getAll: RIO[Console with MessageRepository, Seq[Message]] =
    MessageService.getAllMessages

}
