package org.deblock.exercise.api.http

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class ValidationErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val details =
            ex.bindingResult.fieldErrors.map {
                FieldError(
                    field = it.field,
                    message = it.defaultMessage ?: "Invalid value",
                    rejectedValue = it.rejectedValue
                )
            }
        return badRequest("Validation failed", details, ex.parameter.method?.name)
    }

    @ExceptionHandler(java.lang.Exception::class)
    fun handleUnexpectedException(ex: java.lang.Exception): ResponseEntity<ValidationErrorResponse> =
        badRequest(
            ex.message ?: "Invalid request body",
            emptyList(),
            null
        )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<ValidationErrorResponse> =
        badRequest(
            ex.mostSpecificCause.message ?: "Invalid request body",
            emptyList(),
            null
        )

    private fun badRequest(
        message: String,
        fieldErrors: List<FieldError>,
        path: String?
    ): ResponseEntity<ValidationErrorResponse> =
        ResponseEntity
            .status(BAD_REQUEST)
            .body(
                ValidationErrorResponse(
                    status = BAD_REQUEST.value(),
                    error = BAD_REQUEST.reasonPhrase,
                    message = message,
                    path = path,
                    fieldErrors = fieldErrors
                )
            )
}

data class ValidationErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val path: String? = null,
    val fieldErrors: List<FieldError> = emptyList()
)

data class FieldError(
    val field: String,
    val message: String,
    val rejectedValue: Any?
)