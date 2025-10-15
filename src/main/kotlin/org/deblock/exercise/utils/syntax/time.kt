package org.deblock.exercise.utils.syntax

import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object time {

    val String.isoInstantToOffsetDateTime: OffsetDateTime
        get() =
            Instant.parse(this).atZone(ZoneOffset.systemDefault()).toOffsetDateTime()

    val String.isoLocalDateToOffsetDateTime: OffsetDateTime
        get() =
            LocalDateTime.parse(
                /* text = */ this,
                /* formatter = */ DateTimeFormatter.ISO_LOCAL_DATE_TIME
            ).atZone(ZoneOffset.systemDefault()).toOffsetDateTime()
}