package org.deblock.exercise.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.deblock.exercise.service.web.RestClientService
import org.deblock.exercise.service.web.RestClientServiceImpl
import org.deblock.exercise.settings.sections.RestClientSettings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit.MILLISECONDS

@Configuration
class WebConfig(private val restClientSettings: RestClientSettings) {

    private val responseTimeoutMs = restClientSettings.responseTimeoutMs

    private val responseTimeoutDuration = Duration.ofMillis(responseTimeoutMs)

    @Bean
    fun webClient(): WebClient = with(restClientSettings) {

        val httpClient =
            HttpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutMs)
                .responseTimeout(responseTimeoutDuration)
                .doOnConnected {
                    it.addHandlerLast(ReadTimeoutHandler(readTimeoutMs, MILLISECONDS))
                        .addHandlerLast(WriteTimeoutHandler(writeTimeoutMs, MILLISECONDS))
                }

        WebClient.builder()
            .exchangeStrategies(
                ExchangeStrategies.builder()
                    .codecs { it.defaultCodecs().maxInMemorySize(maxInMemorySize) }
                    .build()
            )
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }

    @Bean
    fun restClientService(): RestClientService = RestClientServiceImpl(webClient(), restClientSettings)
}
