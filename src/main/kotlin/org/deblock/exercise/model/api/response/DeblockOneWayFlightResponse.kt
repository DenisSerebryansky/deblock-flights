package org.deblock.exercise.model.api.response

import java.math.BigDecimal
import java.time.OffsetDateTime

data class DeblockOneWayFlightResponse(
    override val airline: String,
    override val supplier: String,
    override val fare: BigDecimal,
    override val departureAirportCode: String,
    override val destinationAirportCode: String,
    override val departureDate: OffsetDateTime,
    override val arrivalDate: OffsetDateTime,
) : OneWayFlightResponse