package com.akkarimzai.task5.core.application.models.event

import com.akkarimzai.task5.core.application.models.ValidatableDto
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isNotBlank
import org.valiktor.validate
import java.time.LocalDate

data class ListEvents(
    val budget: Double,
    val currency: String,
    val dateFrom: LocalDate,
    val dateTo: LocalDate
) : ValidatableDto() {
    override fun dataValidator() {
        validate(this) {
            validate(ListEvents::budget)
                .isGreaterThan(0.0)

            validate(ListEvents::currency)
                .isNotBlank()
                .hasSize(min = 3, max = 3)
        }
    }
}