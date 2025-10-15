package org.deblock.exercise.service.supplier

import org.deblock.exercise.model.Supplier
import org.deblock.exercise.model.api.request.TwoWayFlightRequest
import org.deblock.exercise.model.api.response.FlightResponse
import org.deblock.exercise.service.web.RestClientService
import org.slf4j.Logger

/**
 * Interface for flight suppliers that fetch flight information from external sources.
 *
 * This interface defines the contract for integrating with different flight supplier APIs,
 * providing a standardized way to retrieve flight data based on two-way flight requests.
 *
 * @param A The supplier-specific request type that will be sent to the external API
 * @param B The flight response type that extends [FlightResponse], representing the supplier's response format
 */
interface FlightsSupplier<A, B : FlightResponse> {

    /**
     * Logger instance for logging flight supplier operations
     */
    val log: Logger

    /**
     * REST client service used to make HTTP requests to the supplier's API
     */
    val restClientService: RestClientService

    /**
     * The supplier identifier representing the external flight provider
     */
    val supplier: Supplier

    /**
     * The URI endpoint for fetching flights from the supplier's API
     */
    val flightsUri: String

    /**
     * The class type of the flight response, used for deserialization
     */
    val responseType: Class<B>

    /**
     * Converts a standard two-way flight request into a supplier-specific request format
     *
     * @param request The two-way flight request containing search criteria
     * @return The supplier-specific request object of type [A]
     */
    fun toSupplierRequest(request: TwoWayFlightRequest): A

    /**
     * Fetches flights from the supplier's API based on the provided request
     *
     * This method converts the request to the supplier-specific format, makes an HTTP call
     * to the supplier's API, and returns the flight results
     *
     * @param request The two-way flight request containing origin, destination, dates, and passenger information
     * @return A [Result] containing a list of flight responses of type [B] on success, or an error on failure
     */
    suspend fun getFlights(request: TwoWayFlightRequest): Result<List<B>> {
        log.debug("Fetching flights from {} ({}) for {}", supplier, flightsUri, request)
        return restClientService.getList(flightsUri, toSupplierRequest(request), responseType)
    }
}