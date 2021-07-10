package dev.insideyou.playground.domain.model.error

sealed trait PersistenceError

object PersistenceError {
  final case class UnexpectedPersistenceError(err: Throwable) extends PersistenceError
}
