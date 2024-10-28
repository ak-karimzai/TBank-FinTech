package com.akkarimzai.task5.core.application.models.currency

data class ConvertedCurrency(
    val fromCurrency: String,
    val toCurrency: String,
    val convertedAmount: Double
)