package org.deblock.exercise.api.http

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.deblock.exercise.model.api.request.TwoWayFlightRequest
import org.deblock.exercise.model.api.response.OneWayFlightResponse
import org.deblock.exercise.service.FlightService
import org.deblock.exercise.utils.shortLogger
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "search")
@RequestMapping("/api/v1/search")
@Validated
class SearchApi(private val flightService: FlightService) {

    private val log = shortLogger()

    @RequestMapping(method = [RequestMethod.POST])
    @Operation(summary = "Search for flights")
    suspend fun search(@Valid @RequestBody request: TwoWayFlightRequest): List<OneWayFlightResponse> {
        log.info("Received search request: $request")
        return flightService.getFlights(request).getOrThrow()
    }
}