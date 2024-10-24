package com.akkarimzai.task10.models.event

import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank

import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.validate
import java.time.LocalDateTime

data class CreateEventCommand(
    val name: String,
    val date: LocalDateTime,
    val tagline: String?
): ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(CreateEventCommand::name)
                .isNotBlank()
                .hasSize(min = 3, max = 60)

            tagline?.let {
                validate(CreateEventCommand::tagline)
                    .isNotBlank()
            }
        }
    }
}