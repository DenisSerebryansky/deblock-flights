package org.deblock.exercise.model.api.response

import java.time.OffsetDateTime

interface TwoWayFlightResponse : FlightResponse {
    val outboundDate: OffsetDateTime
    val inboundDate: OffsetDateTime
}