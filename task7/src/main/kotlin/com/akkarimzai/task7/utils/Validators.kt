package com.akkarimzai.task7.utils

import java.util.*

class Validators {
    companion object {
        fun isValidCurrency(currency: String?): Boolean {
            var result = false

            currency?.let {
                try {
                    Currency.getInstance(currency.uppercase())
                    result = true
                } catch (_: IllegalArgumentException) {
                }
            }
            return result
        }
    }
}