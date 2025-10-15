package org.deblock.exercise.model.api.response

import java.time.OffsetDateTime

interface OneWayFlightResponse : FlightResponse {

    val departureDate: OffsetDateTime
    val arrivalDate: OffsetDateTime

    override fun flatten(): List<OneWayFlightResponse> = listOf(this)
}