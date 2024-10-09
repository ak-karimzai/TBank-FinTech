package com.akkarimzai.task7.models

data class ConvertedCurrencyDto(
    val fromCurrency: String,
    val toCurrency: String,
    val convertedAmount: Double
)