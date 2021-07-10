package dev.insideyou.playground.domain.model
import zio._

import dev.insideyou.playground.domain.model.error.PersistenceError

object MessageRepository {

  type MessageRepository = Has[MessageRepository.Service]

  //TODO: Extract to IMPL class with 2 different implementations
  def save(
      twitterMessage: Message
    ): ZIO[MessageRepository, PersistenceError, Message] =
    ZIO.serviceWith(_.save(twitterMessage))

  def get(id: String): ZIO[MessageRepository, PersistenceError, Option[Message]] =
    ZIO.serviceWith(_.get(id))

  def getAll: ZIO[MessageRepository, PersistenceError, Seq[Message]] =
    ZIO.serviceWith(_.getAll)

  def delete(id: String): ZIO[MessageRepository, PersistenceError, Option[Message]] =
    ZIO.serviceWith(_.delete(id))

  trait Service {
    def save(twitterMessage: Message): IO[PersistenceError, Message]
    def get(id: String): IO[PersistenceError, Option[Message]]
    def getAll: IO[PersistenceError, Seq[Message]]
    def delete(id: String): IO[PersistenceError, Option[Message]]
  }

}
