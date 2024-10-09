package com.akkarimzai.task7.models

import com.akkarimzai.task7.annotations.ValidCurrency
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank


data class ConvertCurrencyCommand(
    @NotBlank
    @get:ValidCurrency(message = "fromCurrency: doesn't exist.")
    val fromCurrency: String,
    @NotBlank
    @get:ValidCurrency(message = "toCurrency: doesn't exist.")
    val toCurrency: String,
    @get:DecimalMin("0.0", inclusive = false, message = "amount: must be greater than zero")
    val amount: Double
)