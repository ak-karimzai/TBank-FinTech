package com.akkarimzai.task5.core.application.models.currency

data class ConvertCurrencyCommand(
    val fromCurrency: String,
    val toCurrency: String,
    val amount: Double
)