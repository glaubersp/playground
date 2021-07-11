package dev.insideyou.playground.infrastructure.environments

import zio._
import dev.insideyou.playground.domain.model.MessageRepository._
import dev.insideyou.playground.infrastructure.persistence.InMemoryMessageRepository

object InMemoryMessageRepositoryEnv {

  val live: Layer[Nothing, MessageRepository] =
    ZLayer.succeed(InMemoryMessageRepository)

}
