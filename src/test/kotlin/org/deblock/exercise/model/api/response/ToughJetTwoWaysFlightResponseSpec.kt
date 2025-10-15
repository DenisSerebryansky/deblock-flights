package org.deblock.exercise.model.api.response

import org.deblock.exercise.SpecBase
import org.deblock.exercise.utils.syntax.number.bd
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ToughJetTwoWaysFlightResponseSpec : SpecBase {

    @Test
    fun `Correct fare evaluation`() {
        ToughJetTwoWaysFlightResponse.getFare(
            basePrice = 100.bd,
            tax = 20.bd,
            discountPercentage = 10.bd
        ).assertEqualsTo(108) // (100 + 20) * 0.9 = 108
    }

    @Test
    fun `Negative fare is impossible`() {
        ToughJetTwoWaysFlightResponse.getFare(
            basePrice = 100.bd,
            tax = 20.bd,
            discountPercentage = 1000.bd
        ).assertEqualsTo(0)
    }

    @Test
    fun `2 decimals precision max`() {
        ToughJetTwoWaysFlightResponse.getFare(
            basePrice = 100.bd,
            tax = 0.bd,
            discountPercentage = 3.375.bd
        ).assertEqualsTo(96.63) // 100 * (100 - 3.375) / 100 = 96.625
    }

    @Test
    fun `Correct flatten`() {

        val twoWayResponse =
            ToughJetTwoWaysFlightResponse(
                airline = "Ryanair",
                basePrice = 19.9.bd,
                tax = 5.99.bd,
                discountPercentage = 10.0.bd,
                departureAirportCode = "LHR",
                destinationAirportCode = "CGD",
                outboundDateTimeIsoInstant = "2025-10-15T12:44:00+00:00",
                inboundDateTimeIsoInstant = "2025-10-16T15:16:00+01:00"
            )

        val oneWayResponses = twoWayResponse.flatten()

        assertEquals(2, oneWayResponses.size)

        val first = oneWayResponses[0]
        val second = oneWayResponses[1]

        assertEquals("LHR", first.departureAirportCode)
        assertEquals("CGD", first.destinationAirportCode)
        first.fare.assertEqualsTo(11.66) // (19.9 + 5.99) * 0.9 / 2 = 11.6505 = 11.66
        assertEquals(twoWayResponse.outboundDate, first.departureDate) // departure as outbound
        assertEquals(twoWayResponse.outboundDate, first.arrivalDate)   // arrival as outbound

        assertEquals("CGD", second.departureAirportCode)
        assertEquals("LHR", second.destinationAirportCode)
        second.fare.assertEqualsTo(11.66) // (19.9 + 5.99) * 0.9 / 2 = 11.6505 = 11.66
        assertEquals(twoWayResponse.inboundDate, second.departureDate) // departure as inbound
        assertEquals(twoWayResponse.inboundDate, second.arrivalDate)   // arrival as inbound
    }
}