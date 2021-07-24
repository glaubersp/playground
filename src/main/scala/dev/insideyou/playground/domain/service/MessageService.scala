package dev.insideyou.playground.domain.service

import zio._

import dev.insideyou.playground.domain.model._
import dev.insideyou.playground.domain.model.error.BusinessError._
import dev.insideyou.playground.domain.model.error.ValidationError.InvalidInputError
import dev.insideyou.playground.infrastructure.persistence.MessageRepository

object MessageService {

  def storeMessage(request: ComposeMessageRequest): RIO[Has[MessageRepository], Message] =
    Message(content = request.content) match {
      case None => ZIO.fail(InvalidInputError(s"Invalid Input: ${request.content}"))
      case Some(message) =>
        MessageRepository.get(message.id).flatMap {
          case Some(_) =>
            ZIO.fail(MessageAlreadyExistsError(s"Message already exists: ${message.id}"))
          case None => MessageRepository.save(message)
        }
    }

  def updateMessage(
      request: UpdateMessageRequest
    ): RIO[Has[MessageRepository], Message] =
    for {
      oldMessage <- Message(content = request.content) match {
        case None => RIO.fail(InvalidInputError(s"Invalid Input: ${request.content}"))
        case Some(_) =>
          MessageRepository.get(request.id).flatMap {
            case None =>
              RIO.fail(MessageDoesNotExistError(s"Message does not exist: ${request.content}"))
            case Some(message) => RIO.succeed(message)
          }
      }
      updatedMessage <- MessageRepository
        .save(oldMessage.copy(content = request.content))
        .orElseFail(InternalError("Error updating message."))
    } yield updatedMessage

  def getMessage(id: String): RIO[Has[MessageRepository], Option[Message]] =
    MessageRepository.get(id)

  def getAllMessages: RIO[Has[MessageRepository], Seq[Message]] =
    MessageRepository.getAll

  def deleteMessage(id: String): RIO[Has[MessageRepository], Option[Message]] =
    MessageRepository.delete(id)

  final case class ComposeMessageRequest(content: String)

  final case class UpdateMessageRequest(id: String, content: String)
}
