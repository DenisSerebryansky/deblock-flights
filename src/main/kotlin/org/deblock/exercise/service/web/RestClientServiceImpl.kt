package org.deblock.exercise.service.web

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.deblock.exercise.settings.sections.RestClientSettings
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.util.retry.Retry
import java.time.Duration

class RestClientServiceImpl(
    private val client: WebClient,
    private val restClientSettings: RestClientSettings
) : RestClientService {

    /**
     * Performs an HTTP POST request to retrieve a list of items from the specified URI.
     *
     * This function sends a POST request with the provided request body, retrieves the response
     * as a flux of items, and collects them into a list. The operation is executed on the IO dispatcher
     * and includes automatic retry logic for 5xx server errors using exponential backoff.
     *
     * @param A the type of the request body
     * @param B the type of the response items
     * @param uri the URI endpoint to send the POST request to
     * @param request the request body to be sent as JSON
     * @param responseType the class type of the response items to deserialize into
     * @return a [Result] containing either a [List] of items of type [B] on success,
     *         or an exception on failure
     */
    override suspend fun <A, B> getList(uri: String, request: A, responseType: Class<B>): Result<List<B>> =
        runCatching {
            withContext(Dispatchers.IO) {
                client
                    .post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request as Any)
                    .retrieve()
                    .bodyToFlux(responseType)
                    .collectList()
                    .retryWhen(
                        with(restClientSettings) {
                            Retry
                                .backoff(retryAttempts, Duration.ofMillis(retryBackoffMs))
                                .filter { it is WebClientResponseException && it.statusCode.is5xxServerError }
                        }
                    )
                    .awaitSingle()
            }
        }
}