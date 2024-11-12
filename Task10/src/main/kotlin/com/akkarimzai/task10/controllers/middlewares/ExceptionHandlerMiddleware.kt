package com.akkarimzai.task10.controllers.middlewares

import com.akkarimzai.task10.exceptions.*
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

    @ExceptionHandler(ConflictException::class)
    fun handleConflictException(e: ConflictException): ResponseEntity<ErrorResponse<String>> =
        ResponseEntity<ErrorResponse<String>>(ErrorResponse(
            HttpStatus.CONFLICT.value(), e.message ?: "conflict."), HttpStatus.CONFLICT)

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<ErrorResponse<String>> =
        ResponseEntity<ErrorResponse<String>>(ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(), e.message ?: "conflict."), HttpStatus.UNAUTHORIZED)
}