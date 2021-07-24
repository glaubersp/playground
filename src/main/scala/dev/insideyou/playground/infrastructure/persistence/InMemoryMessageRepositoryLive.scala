package dev.insideyou.playground.infrastructure.persistence

import scala.collection.mutable

import zio.IO

import dev.insideyou.playground.domain.model._
import dev.insideyou.playground.domain.model.error.PersistenceError.UnexpectedPersistenceError

final case class InMemoryMessageRepositoryLive() extends MessageRepository {

  private val db: mutable.HashMap[String, Message] = new mutable.HashMap()

  def save(message: Message): IO[UnexpectedPersistenceError, Message] =
    db.put(message.id, message) match {
      case Some(_) => IO.succeed(message)
      case None    => IO.succeed(message)
    }

  def get(id: String): IO[UnexpectedPersistenceError, Option[Message]] =
    IO.succeed(db.get(id))

  def getAll: IO[UnexpectedPersistenceError, Seq[Message]] = IO.succeed(db.values.toSeq)

  def delete(id: String): IO[UnexpectedPersistenceError, Option[Message]] = {
    val temp = db.remove(id)
    IO.succeed(temp)
  }
}
