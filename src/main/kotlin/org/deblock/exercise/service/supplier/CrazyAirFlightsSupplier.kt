package org.deblock.exercise.service.supplier

import org.deblock.exercise.model.Supplier.CRAZY_AIR
import org.deblock.exercise.model.api.request.CrazyAirTwoWayFlightRequest
import org.deblock.exercise.model.api.request.TwoWayFlightRequest
import org.deblock.exercise.model.api.response.CrazyAirOneWayFlightResponse
import org.deblock.exercise.service.web.RestClientService
import org.deblock.exercise.settings.sections.CrazyAirSettings
import org.deblock.exercise.utils.shortLogger
import org.springframework.stereotype.Service

@Service
class CrazyAirFlightsSupplier(
    override val restClientService: RestClientService,
    private val crazyAirSettings: CrazyAirSettings
) : FlightsSupplier<CrazyAirTwoWayFlightRequest, CrazyAirOneWayFlightResponse> {

    override val log = shortLogger()

    override val supplier get() = CRAZY_AIR

    override val flightsUri get() = crazyAirSettings.url

    override val responseType = CrazyAirOneWayFlightResponse::class.java

    override fun toSupplierRequest(request: TwoWayFlightRequest): CrazyAirTwoWayFlightRequest =
        CrazyAirTwoWayFlightRequest(
            origin = request.origin,
            destination = request.destination,
            departureDate = request.departureDate.toString(),
            returnDate = request.returnDate.toString(),
            passengerCount = request.numberOfPassengers
        )
}