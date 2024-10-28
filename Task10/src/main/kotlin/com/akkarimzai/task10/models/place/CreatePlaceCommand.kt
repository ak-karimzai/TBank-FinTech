package com.akkarimzai.task10.models.place

import com.akkarimzai.task10.models.common.ValidatableCQ
import com.akkarimzai.task10.models.event.CreateEventCommand
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class CreatePlaceCommand(
    val name: String,
    val address: String,
    val description: String,
    val subway: String?
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(CreatePlaceCommand::name)
                .isNotBlank()
                .hasSize(min = 3, max = 60)

            validate(CreatePlaceCommand::address)
                .isNotBlank()
                .hasSize(min = 3, max = 256)

            validate(CreatePlaceCommand::description)
                .isNotBlank()
                .hasSize(min = 3, max = 256)

            subway?.let {
                validate(CreatePlaceCommand::subway)
                    .isNotBlank()
                    .hasSize(min = 3, max = 60)
            }
        }
    }
}