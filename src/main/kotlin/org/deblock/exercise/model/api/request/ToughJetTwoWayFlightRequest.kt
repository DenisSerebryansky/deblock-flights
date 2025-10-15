package org.deblock.exercise.model.api.request

data class ToughJetTwoWayFlightRequest(
    val from: String,
    val to: String,
    val outboundDate: String,
    val inboundDate: String,
    val numberOfAdults: Int
)