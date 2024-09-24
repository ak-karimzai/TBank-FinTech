package com.akkarimzai.task5.core.application.models.location

import com.akkarimzai.task5.core.application.models.ValidatableDto
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class CreateLocation(
    val slug: String,
    val name: String
) : ValidatableDto() {
    override fun dataValidator() {
        validate(this) {
            validate(CreateLocation::slug)
                .isNotBlank()
                .hasSize(min = 3, max = 10)

            validate(CreateLocation::name)
                .isNotBlank()
                .hasSize(min = 3, max = 60)
        }
    }
}