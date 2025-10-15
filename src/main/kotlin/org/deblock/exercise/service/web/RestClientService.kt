package org.deblock.exercise.service.web

interface RestClientService {

    suspend fun <A, B> getList(uri: String, request: A, responseType: Class<B>): Result<List<B>>
}