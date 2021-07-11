package dev.insideyou.playground.infrastructure.controller

import zio._
import zio.console._

import dev.insideyou.playground.domain.model.Message
import dev.insideyou.playground.domain.model.MessageRepository.MessageRepository
import dev.insideyou.playground.domain.service.MessageService
import dev.insideyou.playground.domain.service.MessageService._

object MessageConsoleController {
  val create: ZIO[Console with MessageRepository, Any, Message] =
    for {
      _ <- putStrLn("Please type your message:")
      message <- getStrLn
      storedMessage <- MessageService.storeMessage(ComposeMessageRequest(message))
    } yield storedMessage

  val read: ZIO[Console with MessageRepository, Any, Option[Message]] =
    for {
      _ <- putStrLn("Please insert id of Message you want to read:")
      id <- getStrLn
      message <- MessageService.getMessage(id)
    } yield message

  val update: ZIO[Console with MessageRepository, Any, Message] =
    for {
      _ <- putStrLn("Please insert Message id:")
      id <- getStrLn
      _ <- putStrLn("Please insert Message new content:")
      content <- getStrLn
      updatedMessage <- MessageService.updateMessage(UpdateMessageRequest(id, content))
    } yield updatedMessage

  val delete: ZIO[Console with MessageRepository, Any, Option[Message]] =
    for {
      _ <- putStrLn("Please insert id of Message you want to delete:")
      id <- getStrLn
      deletedMessage <- MessageService.deleteMessage(id)
    } yield deletedMessage

  val getAll: ZIO[Console with MessageRepository, Any, Seq[Message]] =
    MessageService.getAllMessages

}
