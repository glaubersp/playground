package dev.insideyou.playground.domain.model

import scala.util.Try

final case class Message private (id: String, content: String)

object Message {
  def apply(content: String): Option[Message] =
    Try((content.slice(0, 20).replace(" ", "_").slice(0, 10).toLowerCase, content.slice(0, 80)))
      .toOption
      .map(tuple => new Message(tuple._1, tuple._2))
}
