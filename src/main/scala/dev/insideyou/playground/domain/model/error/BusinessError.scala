package dev.insideyou.playground.domain.model.error

sealed trait BusinessError

object BusinessError {
  final case class MessageAlreadyExistsError(id: String) extends BusinessError
  final case class MessageDoesNotExistError(id: String) extends BusinessError
  final case class InternalError(id: String) extends BusinessError
}

sealed trait ValidationError extends BusinessError

object ValidationError {
  final case class InvalidInputError(content: String) extends ValidationError
}
