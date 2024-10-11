package com.akkarimzai.task7.controllers

import com.akkarimzai.task7.annotations.ValidCurrency
import com.akkarimzai.task7.models.ConvertCurrencyCommand
import com.akkarimzai.task7.models.ConvertedCurrencyDto
import com.akkarimzai.task7.models.CurrencyInfoDto
import com.akkarimzai.task7.responses.ErrorResponse
import com.akkarimzai.task7.services.CurrencyExchangeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/currencies"])
@Tag(name = "Currencies")
class CurrencyController(
    private val currencyExchangeService: CurrencyExchangeService
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/rates/{currency}")
    @Operation(description = "Курс валют.", summary = "Получить курс валют")
    @ApiResponses(
            value = [
                ApiResponse(
                    description = "Найден курс валют.",
                    responseCode = "200",
                    content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = CurrencyInfoDto::class))]
                ),
                ApiResponse(
                    description = "Валюта недействительна.",
                    responseCode = "400",
                    content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
                ),
                ApiResponse(
                    description = "Валюта действительна, но отсутствует в списке валют.",
                    responseCode = "404",
                    content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
                ),
                ApiResponse(
                    description = "Сервис перевода недоступна.",
                    responseCode = "503",
                    content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
                )
            ]
    )
    fun rate(@Validated @PathVariable @ValidCurrency currency: String) : CurrencyInfoDto {
        logger.info { "Request rate with currency: $currency" }
        return currencyExchangeService.rate(currency)
    }

    @PostMapping("/convert")
    @Operation(description = "Обмен валюты.", summary = "Обменять сумму валюты")
    @ApiResponses(
        value = [
            ApiResponse(
                description = "обмен валюты успешно.",
                responseCode = "200",
                content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ConvertedCurrencyDto::class))]
            ),
            ApiResponse(
                description = "Валюта недействительна.",
                responseCode = "400",
                content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                description = "Валюта действительна, но отсутствует в списке валют.",
                responseCode = "404",
                content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                description = "Сервис перевода недоступна.",
                responseCode = "503",
                content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun convert(@Validated @RequestBody request: ConvertCurrencyCommand) : ConvertedCurrencyDto {
        logger.info { "Request convert: $request" }
        return currencyExchangeService.convert(request)
    }
}