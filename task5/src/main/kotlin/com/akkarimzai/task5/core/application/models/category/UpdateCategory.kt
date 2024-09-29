package com.akkarimzai.task5.core.application.models.category

import com.akkarimzai.task5.core.application.exceptions.BadRequestException
import com.akkarimzai.task5.core.application.models.ValidatableDto
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class UpdateCategory(
    val slug: String?,
    val name: String?
) : ValidatableDto() {
    override fun dataValidator() {
        validate(this) {
            var count = 0

            slug?.let {
                validate(UpdateCategory::slug)
                    .isNotBlank()
                    .hasSize(min = 3, max = 60)
                count++
            }

            name?.let {
                validate(UpdateCategory::name)
                    .isNotBlank()
                    .hasSize(min = 3, max = 60)
                count++
            }

            if (count == 0)
                throw BadRequestException("Nothing to update")
        }
    }
}