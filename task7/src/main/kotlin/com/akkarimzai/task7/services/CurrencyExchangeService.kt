package com.akkarimzai.task7.services

import com.akkarimzai.task7.clients.ICurrencyClient
import com.akkarimzai.task7.exceptions.BadRequestException
import com.akkarimzai.task7.exceptions.NotFoundException
import com.akkarimzai.task7.models.ConvertCurrencyCommand
import com.akkarimzai.task7.models.ConvertedCurrencyDto
import com.akkarimzai.task7.models.CurrencyInfoDto
import com.akkarimzai.task7.responses.Valute
import com.akkarimzai.task7.utils.Validators
import org.springframework.stereotype.Service
import java.util.*

@Service
class CurrencyExchangeService(private val currencyClient: ICurrencyClient) {
    fun rate(currency: String): CurrencyInfoDto {
        if (!Validators.isValidCurrency(currency)) {
            throw BadRequestException("Invalid currency: $currency.")
        }

        val currencyInfo = getCurrencyInfo(currency)

        return CurrencyInfoDto(
            currencyInfo.charCode,
            currencyInfo.value)
    }

    fun convert(request: ConvertCurrencyCommand): ConvertedCurrencyDto {
        val fromCurrency = getCurrencyInfo(request.fromCurrency)
        val toCurrency = getCurrencyInfo(request.toCurrency)

        if (fromCurrency.charCode == toCurrency.charCode) {
            throw BadRequestException("from and to currency are same.")
        }

        var convertedAmount = 0.0
        if (fromCurrency.charCode == "RUB") {
            convertedAmount = request.amount / toCurrency.unitRate
        } else if (toCurrency.charCode == "RUB") {
            convertedAmount = fromCurrency.unitRate * request.amount
        } else {
            val amountsInRubles = fromCurrency.unitRate * request.amount
            convertedAmount = amountsInRubles / toCurrency.unitRate
        }

        return ConvertedCurrencyDto(
            request.fromCurrency,
            request.toCurrency,
            convertedAmount)
    }

    private fun getCurrencyInfo(currency: String): Valute {
        val currencyInUpperCase = currency.uppercase()

        if (currencyInUpperCase == "RUB") {
            return Valute(currencyInUpperCase, 1.0, 1.0)
        }

        return currencyClient.fetchAvailableCurrencies()
            .valutes
            .firstOrNull { it.charCode == currency.uppercase(Locale.getDefault()) }
            ?: throw NotFoundException("Currency", currency)
    }
}