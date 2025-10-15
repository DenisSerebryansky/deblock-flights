package org.deblock.exercise.config

import jakarta.annotation.PostConstruct
import org.deblock.exercise.settings.SettingsSection
import org.deblock.exercise.utils.shortLogger
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan("org.deblock.exercise.settings.sections")
class SystemConfig(private val sections: List<SettingsSection>) {

    private val log = shortLogger()

    @PostConstruct
    fun printSystemSettings(): Unit =
        log.info(
            "Deblock Flights has been started and available at http://localhost:8080/swagger-ui/index.html" +
                    ". The following settings have been used:" +
                    "\n\n${sections.joinToString("\n")}"
        )
}