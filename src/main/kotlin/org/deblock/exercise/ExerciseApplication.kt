package org.deblock.exercise

import org.deblock.exercise.utils.banner
import org.deblock.exercise.utils.classPathProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class ExerciseApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder(ExerciseApplication::class.java)
        .banner(banner())
        .properties(classPathProperties("git.properties"))
        .run(*args)
}
