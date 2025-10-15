package org.deblock.exercise.service.supplier

import org.deblock.exercise.model.Supplier.TOUGH_JET
import org.deblock.exercise.model.api.request.ToughJetTwoWayFlightRequest
import org.deblock.exercise.model.api.request.TwoWayFlightRequest
import org.deblock.exercise.model.api.response.ToughJetTwoWaysFlightResponse
import org.deblock.exercise.service.web.RestClientService
import org.deblock.exercise.settings.sections.ToughJetSettings
import org.deblock.exercise.utils.shortLogger
import org.springframework.stereotype.Service

@Service
class ToughJetFlightsSupplier(
    override val restClientService: RestClientService,
    private val toughJetSettings: ToughJetSettings
) : FlightsSupplier<ToughJetTwoWayFlightRequest, ToughJetTwoWaysFlightResponse> {

    override val log = shortLogger()

    override val supplier = TOUGH_JET

    override val flightsUri = toughJetSettings.url

    override val responseType = ToughJetTwoWaysFlightResponse::class.java

    override fun toSupplierRequest(request: TwoWayFlightRequest): ToughJetTwoWayFlightRequest =
        ToughJetTwoWayFlightRequest(
            from = request.origin,
            to = request.destination,
            outboundDate = request.departureDate.toString(),
            inboundDate = request.returnDate.toString(),
            numberOfAdults = request.numberOfPassengers
        )
}