package com.akkarimzai.task10.models.event

import com.akkarimzai.task10.exceptions.BadRequestException
import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.validate
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import java.time.LocalDateTime

data class UpdateEventCommand(
    val name: String?,
    val date: LocalDateTime?,
    val tagline: String?
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            var count = 0

            name?.let {
                validate(UpdateEventCommand::name)
                    .isNotBlank()
                    .hasSize(min = 3, max = 60)
                count++
            }

            date?.let {
                count++
            }

            tagline?.let {
                validate(UpdateEventCommand::tagline)
                    .isNotBlank()
                count++
            }

            if (count == 0) {
                throw BadRequestException("Nothing to update.")
            }
        }
    }
}
