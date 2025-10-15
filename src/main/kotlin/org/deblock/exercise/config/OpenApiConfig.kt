package org.deblock.exercise.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.deblock.exercise.utils.version
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class OpenApiConfig(private val environment: Environment) {

    @Bean
    fun openApi(): OpenAPI =
        OpenAPI().info(
            Info()
                .title("Deblock Flights")
                .description("The Web interface to the Deblock Flights API")
                .version(environment.version)
        )
}