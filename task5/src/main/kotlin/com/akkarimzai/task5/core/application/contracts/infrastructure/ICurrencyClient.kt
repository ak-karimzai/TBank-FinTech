package com.akkarimzai.task5.core.application.contracts.infrastructure

import com.akkarimzai.task5.core.application.models.currency.ConvertCurrencyCommand
import com.akkarimzai.task5.core.application.models.currency.ConvertedCurrency

interface ICurrencyClient {
    fun convertCurrency(command: ConvertCurrencyCommand): ConvertedCurrency
}