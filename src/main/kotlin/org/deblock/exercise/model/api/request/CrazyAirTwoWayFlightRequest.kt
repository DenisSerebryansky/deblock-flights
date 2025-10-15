package org.deblock.exercise.model.api.request

data class CrazyAirTwoWayFlightRequest(
    val origin: String,
    val destination: String,
    val departureDate: String,
    val returnDate: String,
    val passengerCount: Int
)