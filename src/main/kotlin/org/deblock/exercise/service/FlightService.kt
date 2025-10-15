package org.deblock.exercise.service

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import org.deblock.exercise.model.api.request.TwoWayFlightRequest
import org.deblock.exercise.model.api.response.OneWayFlightResponse
import org.deblock.exercise.service.supplier.FlightsSupplier
import org.deblock.exercise.utils.shortLogger
import org.springframework.stereotype.Service


@Service
class FlightService(private val flightSuppliers: List<FlightsSupplier<*, *>>) {

    private val log = shortLogger()

    /**
     * Retrieves and aggregates flight information from multiple suppliers for a two-way flight request.
     *
     * This function concurrently queries all registered flight suppliers, validates the request dates,
     * transforms two-way flights into one-way flight responses, and returns them sorted by fare.
     * If any supplier fails to return results, the error is logged and an empty list is used for that supplier.
     *
     * @param request The two-way flight request containing departure and return dates, origin, destination,
     *                and passenger information. The return date must be on or after the departure date.
     *
     * @return A [Result] containing a list of [OneWayFlightResponse] sorted by fare in ascending order,
     *         or a failure if the request validation fails or an unexpected error occurs during processing.
     */
    suspend fun getFlights(request: TwoWayFlightRequest): Result<List<OneWayFlightResponse>> =
        runCatching {

            with(request) {
                require(returnDate.isEqual(departureDate) || returnDate.isAfter(departureDate)) {
                    "Return date must be on/after departure date"
                }
            }

            supervisorScope {
                flightSuppliers.map { supplier ->
                    async {
                        supplier.getFlights(request).fold(
                            onSuccess = { it },
                            onFailure = {
                                log.error("Couldn't fetch flights from ${supplier.supplier} for $request", it)
                                listOf()
                            }
                        ).flatMap { it.flatten() } // transform two-way flights into one-way flights
                    }
                }
                    .awaitAll()
                    .flatten()
                    .sortedBy { it.fare }
            }
        }
}