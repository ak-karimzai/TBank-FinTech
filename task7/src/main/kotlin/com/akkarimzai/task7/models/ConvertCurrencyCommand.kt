package com.akkarimzai.task7.models

import com.akkarimzai.task7.annotations.ValidCurrency
import jakarta.validation.constraints.DecimalMin

data class ConvertCurrencyCommand(
    @get:ValidCurrency(message = "fromCurrency: doesn't exist.")
    val fromCurrency: String,
    @get:ValidCurrency(message = "toCurrency: doesn't exist.")
    val toCurrency: String,
    @get:DecimalMin("0.0", inclusive = false, message = "amount: must be greater than zero")
    val amount: Double
)