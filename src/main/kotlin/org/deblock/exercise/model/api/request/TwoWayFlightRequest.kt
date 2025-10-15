package org.deblock.exercise.model.api.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.util.*

data class TwoWayFlightRequest(
    val id: UUID = UUID.randomUUID(),
    @field:NotBlank @field:Size(min = 3, max = 3) val origin: String,
    @field:NotBlank @field:Size(min = 3, max = 3) val destination: String,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    @field:Min(1) @field:Max(4) val numberOfPassengers: Int,
)