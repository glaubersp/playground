package dev.insideyou.playground.domain.model.error

sealed trait PersistenceError extends Throwable

object PersistenceError {
  final case class UnexpectedPersistenceError(err: Throwable) extends PersistenceError
}
