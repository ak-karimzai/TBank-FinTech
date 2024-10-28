package com.akkarimzai.task5.core.application.models.location

import com.akkarimzai.task5.core.application.exceptions.BadRequestException
import com.akkarimzai.task5.core.application.models.ValidatableDto
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class UpdateLocation(
    val slug: String?,
    val name: String?
) : ValidatableDto() {
    override fun dataValidator() {
        validate(this) {
            var count = 0

            slug?.let {
                validate(UpdateLocation::slug)
                    .isNotBlank()
                    .hasSize(min = 3, max = 10)
                count++
            }

            name?.let {
                validate(UpdateLocation::name)
                    .isNotBlank()
                    .hasSize(min = 3, max = 60)
                count++
            }

            if (count == 0)
                throw BadRequestException("Nothing to update")
        }
    }
}