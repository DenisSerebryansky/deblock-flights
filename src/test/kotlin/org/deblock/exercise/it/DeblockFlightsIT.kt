package org.deblock.exercise.it

import com.github.tomakehurst.wiremock.client.WireMock.*
import org.deblock.exercise.SpecBase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class DeblockFlightsIT(@param:Autowired val webTestClient: WebTestClient) : SpecBase {

    @Test
    fun `should return validation failure for invalid requests`() {
        listOf(
            mapOf(
                "origin" to "LHR",
                "destination" to "CDG",
                "departureDate" to "2025-10-15",
                "returnDate" to "2025-10-20",
                "numberOfPassengers" to 5         // invalid number of passengers
            ),
            mapOf(
                "origin" to "LHR",
                "destination" to "CDG",
                "departureDate" to "2025-10-15",
                "returnDate" to "2025-10-20",
                "numberOfPassengers" to -1        // invalid number of passengers
            ),
            mapOf(
                "origin" to "LHRA",               // invalid IATA code
                "destination" to "CDG",
                "departureDate" to "2025-10-15",
                "returnDate" to "2025-10-20",
                "numberOfPassengers" to 1
            ),
            mapOf(
                "origin" to "LHR",
                "destination" to "CDGC",          // invalid IATA code
                "departureDate" to "2025-10-15",
                "returnDate" to "2025-10-20",
                "numberOfPassengers" to 1
            ),
            mapOf(
                "origin" to "LHR",
                "destination" to "CDG",
                "departureDate" to "2025-10-34",  // invalid date
                "returnDate" to "2025-10-20",
                "numberOfPassengers" to 1
            ),
            mapOf(
                "origin" to "LHR",
                "destination" to "CDG",
                "departureDate" to "2025-10-15",
                "returnDate" to "2025-14-20",     // invalid date
                "numberOfPassengers" to 1
            ),
            mapOf(
                "origin" to "LHR",
                "destination" to "CDG",
                "departureDate" to "2025-10-15",
                "returnDate" to "2025-10-14",     // return after departure
                "numberOfPassengers" to 1
            ),
        ).forEach { request ->
            withClue("Checking request: $request") {
                webTestClient
                    .post()
                    .uri("/api/v1/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isBadRequest
            }
        }
    }

    @Test
    fun `should return flights from all suppliers`() {

        stubFor(
            post(urlEqualTo("/crazy-air"))
                .willReturn(
                    okJson(
                        """
                            [
                                {
                                    "airline": "WizzAir",
                                    "price": 25.0,
                                    "departureAirportCode": "LHR",
                                    "destinationAirportCode": "CGD",
                                    "departureDate": "2025-10-15T11:23:45.000",
                                    "arrivalDate": "2025-10-15T14:23:45.000"
                                },
                                {
                                    "airline": "WizzAir",
                                    "price": 22.0,
                                    "departureAirportCode": "CGD",
                                    "destinationAirportCode": "LHR",
                                    "departureDate": "2025-10-15T19:02:13.000",
                                    "arrivalDate": "2025-10-15T21:24:32.000"
                                }
                            ]"""
                    )
                )
        )

        stubFor(
            post(urlEqualTo("/tough-jet"))
                .willReturn(
                    okJson(
                        """
                            [
                                {
                                    "carrier": "Ryanair",
                                    "basePrice": 19.9,
                                    "tax": 5.99,
                                    "discount": 10.0,
                                    "departureAirportName": "LHR",
                                    "arrivalAirportName": "CGD",
                                    "outboundDateTime": "2025-10-15T12:44:00+00:00",
                                    "inboundDateTime": "2025-10-16T15:16:00+01:00"
                                }
                            ]"""
                    )
                )
        )

        val request =
            mapOf(
                "origin" to "LHR",
                "destination" to "CDG",
                "departureDate" to "2025-10-15",
                "returnDate" to "2025-10-20",
                "numberOfPassengers" to 1
            )

        webTestClient
            .post()
            .uri("/api/v1/search")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.length()").isEqualTo(4)
            .jsonPath("$[0].airline").isEqualTo("Ryanair")
    }

    @Test
    fun `should return flights even if some suppliers are unavailable`() {

        stubFor(
            post(urlEqualTo("/crazy-air"))
                .willReturn(
                    okJson(
                        """
                            [
                                {
                                    "airline": "WizzAir",
                                    "price": 25.0,
                                    "departureAirportCode": "LHR",
                                    "destinationAirportCode": "CGD",
                                    "departureDate": "2025-10-15T11:23:45.000",
                                    "arrivalDate": "2025-10-15T14:23:45.000"
                                },
                                {
                                    "airline": "WizzAir",
                                    "price": 22.0,
                                    "departureAirportCode": "CGD",
                                    "destinationAirportCode": "LHR",
                                    "departureDate": "2025-10-15T19:02:13.000",
                                    "arrivalDate": "2025-10-15T21:24:32.000"
                                }
                            ]"""
                    )
                )
        )

        stubFor(
            post(urlEqualTo("/tough-jet"))
                .willReturn(
                    aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"error":"Internal Server Error"}""")
                )
        )

        val request =
            mapOf(
                "origin" to "LHR",
                "destination" to "CDG",
                "departureDate" to "2025-10-15",
                "returnDate" to "2025-10-20",
                "numberOfPassengers" to 1
            )

        webTestClient
            .post()
            .uri("/api/v1/search")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.length()").isEqualTo(2)
            .jsonPath("$[0].airline").isEqualTo("WizzAir")
    }

    @Test
    fun `should return empty list when all suppliers are unavailable`() {

        stubFor(
            post(urlEqualTo("/crazy-air"))
                .willReturn(
                    aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"error":"Internal Server Error"}""")
                )
        )

        stubFor(
            post(urlEqualTo("/tough-jet"))
                .willReturn(
                    aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"error":"Internal Server Error"}""")
                )
        )

        val request =
            mapOf(
                "origin" to "LHR",
                "destination" to "CDG",
                "departureDate" to "2025-10-15",
                "returnDate" to "2025-10-20",
                "numberOfPassengers" to 1
            )

        webTestClient
            .post()
            .uri("/api/v1/search")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.length()").isEqualTo(0)
    }
}
