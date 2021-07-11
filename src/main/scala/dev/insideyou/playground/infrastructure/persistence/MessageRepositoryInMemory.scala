package dev.insideyou.playground.infrastructure.persistence

import dev.insideyou.playground.domain.model._
import dev.insideyou.playground.domain.model.error.PersistenceError
import zio.IO

import scala.collection.mutable

object MessageRepositoryInMemory extends MessageRepository.Service {

  private val db: mutable.HashMap[String, Message] = new mutable.HashMap()

  override def save(message: Message): IO[PersistenceError, Message] =
    db.put(message.id, message) match {
      case Some(_) => IO.succeed(message)
      case None    => IO.succeed(message)
    }

  override def get(id: String): IO[PersistenceError, Option[Message]] =
    IO.succeed(db.get(id))

  override def getAll: IO[PersistenceError, Seq[Message]] = IO.succeed(db.values.toSeq)

  override def delete(id: String): IO[PersistenceError, Option[Message]] = {
    val temp = db.remove(id)
    IO.succeed(temp)
  }

}
