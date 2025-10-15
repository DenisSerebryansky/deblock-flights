package org.deblock.exercise.model.api.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.deblock.exercise.model.Supplier.TOUGH_JET
import org.deblock.exercise.utils.syntax.number.bd
import org.deblock.exercise.utils.syntax.time.isoInstantToOffsetDateTime
import java.math.BigDecimal
import java.math.RoundingMode.CEILING
import java.time.OffsetDateTime

data class ToughJetTwoWaysFlightResponse(
    @param:JsonProperty("carrier") override val airline: String,
    @param:JsonProperty("basePrice") @get:JsonIgnore val basePrice: BigDecimal,
    @param:JsonProperty("tax") @get:JsonIgnore val tax: BigDecimal,
    @param:JsonProperty("discount") @get:JsonIgnore val discountPercentage: BigDecimal,
    @param:JsonProperty("departureAirportName") override val departureAirportCode: String,
    @param:JsonProperty("arrivalAirportName") override val destinationAirportCode: String,
    @param:JsonProperty("outboundDateTime") @get:JsonIgnore val outboundDateTimeIsoInstant: String,
    @param:JsonProperty("inboundDateTime") @get:JsonIgnore val inboundDateTimeIsoInstant: String,
) : TwoWayFlightResponse {

    override val supplier: String get() = TOUGH_JET.name

    override val fare: BigDecimal get() = getFare(basePrice, tax, discountPercentage)

    override val outboundDate: OffsetDateTime get() = outboundDateTimeIsoInstant.isoInstantToOffsetDateTime

    override val inboundDate: OffsetDateTime get() = inboundDateTimeIsoInstant.isoInstantToOffsetDateTime

    /**
     * For simplicity
     *  - we divide round-trip price equally between two one-way flights
     *  - we assume that arrival date is the same as outbound/inbound date
     */
    override fun flatten(): List<OneWayFlightResponse> {

        val fare = getFare(basePrice, tax, discountPercentage)
        val oneWayFare = fare.divide(2.bd, 2, CEILING)
        val supplier = TOUGH_JET.name

        return listOf(
            DeblockOneWayFlightResponse(
                airline = airline,
                supplier = supplier,
                fare = oneWayFare,
                departureAirportCode = departureAirportCode,
                destinationAirportCode = destinationAirportCode,
                departureDate = outboundDate,
                arrivalDate = outboundDate,
            ),
            DeblockOneWayFlightResponse(
                airline = airline,
                supplier = supplier,
                fare = oneWayFare,
                departureAirportCode = destinationAirportCode,
                destinationAirportCode = departureAirportCode,
                departureDate = inboundDate,
                arrivalDate = inboundDate,
            )
        )
    }

    companion object {

        fun getFare(basePrice: BigDecimal, tax: BigDecimal, discountPercentage: BigDecimal): BigDecimal =
            (basePrice + tax)
                .multiply((100.bd - discountPercentage).divide(100.bd))
                .max(0.bd)
                .setScale(2, CEILING)
    }
}
