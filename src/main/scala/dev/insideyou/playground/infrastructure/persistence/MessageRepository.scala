package dev.insideyou.playground.infrastructure.persistence

import zio._

import dev.insideyou.playground.domain.model.Message
import dev.insideyou.playground.domain.model.error.PersistenceError

trait MessageRepository {
  def save(twitterMessage: Message): IO[PersistenceError, Message]
  def get(id: String): IO[PersistenceError, Option[Message]]
  def getAll: IO[PersistenceError, Seq[Message]]
  def delete(id: String): IO[PersistenceError, Option[Message]]
}

object MessageRepository {

  type MessageRepositoryEnvironment = Has[MessageRepository]

  def save(
      twitterMessage: Message
    ): ZIO[MessageRepositoryEnvironment, PersistenceError, Message] =
    ZIO.serviceWith(_.save(twitterMessage))

  def get(id: String): ZIO[MessageRepositoryEnvironment, PersistenceError, Option[Message]] =
    ZIO.serviceWith(_.get(id))

  def getAll: ZIO[MessageRepositoryEnvironment, PersistenceError, Seq[Message]] =
    ZIO.serviceWith(_.getAll)

  def delete(id: String): ZIO[MessageRepositoryEnvironment, PersistenceError, Option[Message]] =
    ZIO.serviceWith(_.delete(id))

}
