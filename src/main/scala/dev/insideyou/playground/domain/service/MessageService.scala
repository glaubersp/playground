package dev.insideyou.playground.domain.service

import dev.insideyou.playground.domain.model.MessageRepository.MessageRepository
import dev.insideyou.playground.domain.model._
import dev.insideyou.playground.domain.model.error.BusinessError._
import dev.insideyou.playground.domain.model.error.ValidationError.InvalidInputError
import zio.ZIO

object MessageService {

  def storeMessage(request: ComposeMessageRequest): ZIO[MessageRepository, Throwable, Message] =
    Message(content = request.content) match {
      case None => ZIO.fail(InvalidInputError(request.content))
      case Some(message) =>
        MessageRepository.get(message.id).flatMap {
          case Some(_) => ZIO.fail(MessageAlreadyExistsError(message.id))
          case None    => MessageRepository.save(message)
        }
    }

  //TODO: Improve Error type
  def updateMessage(
      request: UpdateMessageRequest
    ): ZIO[MessageRepository, Throwable, Message] =
    for {
      oldMessage <- Message(content = request.content) match {
        case None => ZIO.fail(InvalidInputError(request.content))
        case Some(_) =>
          MessageRepository.get(request.id).flatMap {
            case None          => ZIO.fail(MessageDoesNotExistError(request.content))
            case Some(message) => ZIO.succeed(message)
          }
      }
      updatedMessage <- MessageRepository
        .save(oldMessage.copy(content = request.content))
        .orElseFail(InternalError(""))
    } yield updatedMessage

  def getMessage(id: String): ZIO[MessageRepository, Throwable, Option[Message]] =
    MessageRepository.get(id)

  def getAllMessages: ZIO[MessageRepository, Throwable, Seq[Message]] =
    MessageRepository.getAll

  def deleteMessage(id: String): ZIO[MessageRepository, Throwable, Option[Message]] =
    MessageRepository.delete(id)

  final case class ComposeMessageRequest(content: String)
  final case class UpdateMessageRequest(id: String, content: String)
}
