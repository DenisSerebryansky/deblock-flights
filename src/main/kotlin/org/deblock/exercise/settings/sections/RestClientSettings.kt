package org.deblock.exercise.settings.sections

import org.deblock.exercise.settings.SettingsSection
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "deblock.rest-client")
data class RestClientSettings(
    val connectionTimeoutMs: Int,
    val readTimeoutMs: Long,
    val writeTimeoutMs: Long,
    val responseTimeoutMs: Long,
    val maxInMemorySize: Int,
    val retryAttempts: Long,
    val retryBackoffMs: Long
) : SettingsSection {

    override fun toString(): String =
        """deblock.rest-client
            |  connection-timeout-ms : $connectionTimeoutMs
            |  read-timeout-ms       : $readTimeoutMs
            |  write-timeout-ms      : $writeTimeoutMs
            |  response-timeout-ms   : $responseTimeoutMs
            |  max-in-memory-size    : $maxInMemorySize
            |  retry-attempts        : $retryAttempts
            |  retry-backoff-ms      : $retryBackoffMs
            |  
        """.trimMargin()
}