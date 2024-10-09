package com.akkarimzai.task7.annotations

import com.akkarimzai.task7.utils.Validators
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class CurrencyValidator : ConstraintValidator<ValidCurrency, String?> {
    override fun isValid(currency: String?, p1: ConstraintValidatorContext?): Boolean {
        return Validators.isValidCurrency(currency)
    }
}