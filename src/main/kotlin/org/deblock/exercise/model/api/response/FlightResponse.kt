package org.deblock.exercise.model.api.response

import java.math.BigDecimal

/**
 * Represents a flight response containing flight information and pricing details.
 *
 * This interface defines the common structure for flight responses, including airline information,
 * supplier details, fare, and airport codes. Implementations of this interface should provide
 * the ability to flatten complex flight structures into a list of one-way flights.
 */
interface FlightResponse {

    /**
     * The airline operating the flight
     */
    val airline: String

    /**
     * The supplier providing the flight information
     */
    val supplier: String

    /**
     * The total fare for the flight
     */
    val fare: BigDecimal

    /**
     * The IATA code of the departure airport
     */
    val departureAirportCode: String

    /**
     * The IATA code of the destination airport
     */
    val destinationAirportCode: String

    /**
     * Flattens the flight response into a list of one-way flight segments.
     *
     * This method breaks down complex flight structures (such as round-trip or multi-leg flights)
     * into individual one-way flight segments for easier processing and analysis.
     *
     * @return A list of [OneWayFlightResponse] objects representing individual flight segments.
     */
    fun flatten(): List<OneWayFlightResponse>
}