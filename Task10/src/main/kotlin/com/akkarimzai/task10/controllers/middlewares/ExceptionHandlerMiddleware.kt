package com.akkarimzai.task10.controllers.middlewares

import com.akkarimzai.task10.exceptions.BadRequestException
import com.akkarimzai.task10.exceptions.ForbiddenRequestException
import com.akkarimzai.task10.exceptions.NotFoundException
import com.akkarimzai.task10.exceptions.ValidationException
import com.akkarimzai.task10.responses.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(e: ValidationException): ResponseEntity<ErrorResponse<List<String>>> =
        ResponseEntity<ErrorResponse<List<String>>>(ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), e.validationResult), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(ForbiddenRequestException::class)
    fun handleForbiddenException(e: ForbiddenRequestException): ResponseEntity<ErrorResponse<String>> =
        ResponseEntity<ErrorResponse<String>>(ErrorResponse(
            HttpStatus.FORBIDDEN.value(), e.message ?: "forbidden."), HttpStatus.FORBIDDEN)
}