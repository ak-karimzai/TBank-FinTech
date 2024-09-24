package com.akkarimzai.task5.api.middlwares

import com.akkarimzai.task5.api.models.ErrorResponse
import com.akkarimzai.task5.core.application.exceptions.BadRequestException
import com.akkarimzai.task5.core.application.exceptions.NotFoundException
import com.akkarimzai.task5.core.application.exceptions.ValidationException
import com.akkarimzai.task5.persistence.annotations.logging.LogExecutionTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@LogExecutionTime
class ExceptionHandlerMiddleware : ResponseEntityExceptionHandler() {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: BadRequestException): ResponseEntity<ErrorResponse<String>> {
        return ResponseEntity<ErrorResponse<String>>(ErrorResponse(e.message ?: "Not found"), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException): ResponseEntity<ErrorResponse<String>> {
        return ResponseEntity<ErrorResponse<String>>(ErrorResponse(e.message ?: "Bad Request"), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(e: ValidationException): ResponseEntity<ErrorResponse<List<String>>> {
        return ResponseEntity<ErrorResponse<List<String>>>(ErrorResponse(e.validationResult), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleInvalidArgumentException(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse<String>> {
        return ResponseEntity<ErrorResponse<String>>(ErrorResponse("invalid id"), HttpStatus.BAD_REQUEST)
    }
}