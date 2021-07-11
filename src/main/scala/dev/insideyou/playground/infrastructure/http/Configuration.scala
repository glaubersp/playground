package dev.insideyou.playground.infrastructure.http

import pureconfig.ConfigSource
import zio._

object Configuration {

  type Configuration = Has[ApiConfig]
  val apiConfig: URIO[Has[ApiConfig], ApiConfig] = ZIO.service

  final case class AppConfig(api: ApiConfig)

  final case class ApiConfig(endpoint: String, port: Int)

  object Configuration {
    import pureconfig.generic.auto._
    val live: Layer[Throwable, Configuration] = Task
      .effect(ConfigSource.default.loadOrThrow[AppConfig])
      .map(c => Has(c.api))
      .toLayerMany
  }
}
