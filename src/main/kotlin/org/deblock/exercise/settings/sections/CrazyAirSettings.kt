package org.deblock.exercise.settings.sections

import org.deblock.exercise.settings.SettingsSection
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "crazy-air")
data class CrazyAirSettings(val url: String) : SettingsSection {

    override fun toString(): String =
        """crazy-air
            |  url : $url
            |  
        """.trimMargin()
}
