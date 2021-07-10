package dev.insideyou.playground.infrastructure.persistence

import scala.collection.mutable

import zio.IO

import dev.insideyou.playground.domain.model._
import dev.insideyou.playground.domain.model.error.PersistenceError
import dev.insideyou.playground.domain.model.error.PersistenceError.UnexpectedPersistenceError

object MessageRepositoryInMemory extends MessageRepository.Service {

  private val db: mutable.HashMap[String, Message] = new mutable.HashMap()

  override def save(message: Message): IO[PersistenceError, Message] =
    IO.fromOption(
      db.put(message.id, message)
        .flatMap(_ => Some(message))
    ).orElseFail(UnexpectedPersistenceError(new Throwable("Error saving message!")))

  override def get(id: String): IO[PersistenceError, Option[Message]] =
    IO.succeed(db.get(id))

  override def getAll: IO[PersistenceError, Seq[Message]] = IO.succeed(db.values.toSeq)

  override def delete(id: String): IO[PersistenceError, Option[Message]] =
    IO.succeed(db.remove(id))

}
