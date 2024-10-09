package com.akkarimzai.task7.controllers

import com.akkarimzai.task7.annotations.ValidCurrency
import com.akkarimzai.task7.models.ConvertCurrencyCommand
import com.akkarimzai.task7.models.ConvertedCurrencyDto
import com.akkarimzai.task7.models.CurrencyInfoDto
import com.akkarimzai.task7.services.CurrencyExchangeService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/currencies"])
class CurrencyController(
    private val currencyExchangeService: CurrencyExchangeService
) {
    @GetMapping("/rates/{currency}")
    fun rate(@Validated @PathVariable @ValidCurrency currency: String) : CurrencyInfoDto {
        return currencyExchangeService.rate(currency)
    }

    @PostMapping("/convert")
    fun convert(@Validated @RequestBody request: ConvertCurrencyCommand) : ConvertedCurrencyDto {
        return currencyExchangeService.convert(request)
    }
}