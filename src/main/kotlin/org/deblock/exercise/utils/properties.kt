package org.deblock.exercise.utils

import org.springframework.boot.Banner
import org.springframework.boot.ansi.AnsiColor
import org.springframework.boot.ansi.AnsiOutput
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import java.io.PrintStream
import java.util.*

fun classPathProperties(fileName: String): Properties =
    Properties().apply { load(ClassPathResource(fileName).inputStream) }

val Environment.version: String
    get() = getProperty("git.commit.id.describe")?.takeIf { it.isNotBlank() } ?: "unknown"

// @formatter:off
fun banner(): Banner = Banner { environment, _, out ->
    with(out) {
        colored("""    ____       __    __           __      _________       __  __        """)
        colored("""   / __ \___  / /_  / /___  _____/ /__   / ____/ (_)___ _/ /_/ /_  _____""")
        colored("""  / / / / _ \/ __ \/ / __ \/ ___/ //_/  / /_  / / / __ `/ __/ __ \/ ___/""")
        colored(""" / /_/ /  __/ /_/ / / /_/ / /__/ ,<    / __/ / / / /_/ / /_/ / / (__  ) """)
        colored("""/_____/\___/_.___/_/\____/\___/_/|_|  /_/   /_/_/\__, /\__/_/ /_/____/  """)
        colored("""                                                /____/  """, AnsiColor.RED, environment.version, AnsiColor.DEFAULT)
        colored()
    }
}
// @formatter:on

fun PrintStream.colored(vararg elements: Any) = println(AnsiOutput.toString(*elements))