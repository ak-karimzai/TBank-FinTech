package com.akkarimzai.task7.middlewares

import com.akkarimzai.task7.exceptions.BadRequestException
import com.akkarimzai.task7.exceptions.NotFoundException
import com.akkarimzai.task7.exceptions.ServiceUnavailableException
import com.akkarimzai.task7.responses.ErrorResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerMiddleware {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: Exception): ResponseEntity<ErrorResponse<String>> =
        ResponseEntity<ErrorResponse<String>>(ErrorResponse(
            HttpStatus.NOT_FOUND.value(), e.message ?: "Not Found."), HttpStatus.NOT_FOUND)

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: Exception): ResponseEntity<ErrorResponse<String>> =
        ResponseEntity<ErrorResponse<String>>(ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), e.message ?: "Bad Request."), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(ServiceUnavailableException::class)
    fun handleServiceUnavailableException(
        e: Exception, response: HttpServletResponse): ResponseEntity<ErrorResponse<String>> {
        response.addHeader("Retry-After", "3600")
        return ResponseEntity<ErrorResponse<String>>(
            ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), e.message ?: "Service unavailable."), HttpStatus.SERVICE_UNAVAILABLE)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse<List<String?>>> {
        val validationResult = e.fieldErrors.map { it.defaultMessage }
        return ResponseEntity<ErrorResponse<List<String?>>>(
            ErrorResponse(HttpStatus.BAD_REQUEST.value(), validationResult), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMethodArgumentNotValidException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse<String>> =
        ResponseEntity<ErrorResponse<String>>(
            ErrorResponse(HttpStatus.BAD_REQUEST.value(), "missing some fields in request body."), HttpStatus.BAD_REQUEST)

}