package dev.insideyou.playground.domain.model.error

sealed trait WebError extends Throwable {
  def message: String
  override def getMessage: String = message
}

object WebError {
  final case class ConfigurationError(text: String) extends WebError {
    def message: String = text
  }

  final case object MissingBotToken extends WebError {
    def message: String = "Bot token is not set as environment variable"
  }

  final case class NotFound(url: String) extends WebError {
    def message: String = s"$url not found"
  }

  final case class MalformedUrl(url: String) extends WebError {
    def message: String = s"Couldn't build url for repository: $url"
  }

  final case class UnexpectedError(text: String) extends WebError {
    def message: String = text
  }

}
