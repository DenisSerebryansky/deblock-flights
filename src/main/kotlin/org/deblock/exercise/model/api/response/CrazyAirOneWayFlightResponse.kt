package org.deblock.exercise.model.api.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.deblock.exercise.model.Supplier.CRAZY_AIR
import org.deblock.exercise.utils.syntax.time.isoLocalDateToOffsetDateTime
import java.math.BigDecimal
import java.time.OffsetDateTime

data class CrazyAirOneWayFlightResponse(
    override val airline: String,
    @param:JsonProperty("price") override val fare: BigDecimal,
    override val departureAirportCode: String,
    override val destinationAirportCode: String,
    @param:JsonProperty("departureDate") @get:JsonIgnore val departureDateIsoLocalDateTime: String,
    @param:JsonProperty("arrivalDate") @get:JsonIgnore val arrivalDateIsoLocalDateTime: String,
) : OneWayFlightResponse {

    override val supplier: String get() = CRAZY_AIR.name

    override val departureDate: OffsetDateTime get() = departureDateIsoLocalDateTime.isoLocalDateToOffsetDateTime

    override val arrivalDate: OffsetDateTime get() = arrivalDateIsoLocalDateTime.isoLocalDateToOffsetDateTime
}
