package dev.insideyou.playground.infrastructure.persistence

import dev.insideyou.playground.domain.model._
import dev.insideyou.playground.domain.model.error.PersistenceError.UnexpectedPersistenceError
import zio.IO

import scala.collection.mutable

object InMemoryMessageRepository extends MessageRepository.Service {

  private val db: mutable.HashMap[String, Message] = new mutable.HashMap()

  override def save(message: Message): IO[UnexpectedPersistenceError, Message] =
    db.put(message.id, message) match {
      case Some(_) => IO.succeed(message)
      case None    => IO.succeed(message)
    }

  override def get(id: String): IO[UnexpectedPersistenceError, Option[Message]] =
    IO.succeed(db.get(id))

  override def getAll: IO[UnexpectedPersistenceError, Seq[Message]] = IO.succeed(db.values.toSeq)

  override def delete(id: String): IO[UnexpectedPersistenceError, Option[Message]] = {
    val temp = db.remove(id)
    IO.succeed(temp)
  }

}
