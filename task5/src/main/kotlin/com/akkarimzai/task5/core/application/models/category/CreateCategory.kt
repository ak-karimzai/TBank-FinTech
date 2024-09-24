package com.akkarimzai.task5.core.application.models.category

import com.akkarimzai.task5.core.application.models.ValidatableDto
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class CreateCategory (
    val slug: String,
    val name: String
) : ValidatableDto() {
    override fun dataValidator() {
        validate(this) {
            validate(CreateCategory::slug)
                .isNotBlank()
                .hasSize(min = 3, max = 60)

            validate(CreateCategory::name)
                .isNotBlank()
                .hasSize(min = 3, max = 60)
        }
    }
}