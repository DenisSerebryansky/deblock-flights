package org.deblock.exercise.settings.sections

import org.deblock.exercise.settings.SettingsSection
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "tough-jet")
data class ToughJetSettings(val url: String) : SettingsSection {

    override fun toString(): String =
        """tough-jet
            |  url : $url
            |  
        """.trimMargin()
}
