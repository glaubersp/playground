package dev.insideyou.playground.infrastructure.persistence

import scala.collection.mutable
import zio.{ IO, ULayer, ZLayer }
import dev.insideyou.playground.domain.model._
import dev.insideyou.playground.domain.model.error.PersistenceError.UnexpectedPersistenceError
import dev.insideyou.playground.infrastructure.persistence.MessageRepository.MessageRepositoryEnvironment

object InMemoryMessageRepository {
  val live: ULayer[MessageRepositoryEnvironment] =
    ZLayer.succeed(InMemoryMessageRepository())
}

final case class InMemoryMessageRepository() extends MessageRepository {

  private val db: mutable.HashMap[String, Message] = new mutable.HashMap()

  def save(message: Message): IO[UnexpectedPersistenceError, Message] =
    db.put(message.id, message) match {
      case Some(_) => IO.succeed(message)
      case None    => IO.succeed(message)
    }

  def get(id: String): IO[UnexpectedPersistenceError, Option[Message]] =
    IO.succeed(db.get(id))

  def getAll: IO[UnexpectedPersistenceError, Seq[Message]] = IO.succeed(db.values.toSeq.sortBy(_.id))

  def delete(id: String): IO[UnexpectedPersistenceError, Option[Message]] = {
    val temp = db.remove(id)
    IO.succeed(temp)
  }
}
