package dev.insideyou.playground.infrastructure.environments

import zio._

import dev.insideyou.playground.domain.model.MessageRepository
import dev.insideyou.playground.infrastructure.persistence.MessageRepositoryInMemory

object MessageRepositoryEnv {

  val inMemory: Layer[Nothing, Has[MessageRepository.Service]] =
    ZLayer.succeed(MessageRepositoryInMemory)

}
