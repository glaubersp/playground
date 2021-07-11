package dev.insideyou.playground.infrastructure.controller

import dev.insideyou.playground.domain.model.Message
import dev.insideyou.playground.domain.model.MessageRepository.MessageRepository
import dev.insideyou.playground.domain.service.MessageService
import dev.insideyou.playground.domain.service.MessageService.ComposeMessageRequest
import zio.RIO

object MessageWebController {

  def create(content: String): RIO[MessageRepository, Message] =
    for {
      message <- MessageService
        .storeMessage(ComposeMessageRequest(content))
    } yield message
}
