package com.akkarimzai.task10.models.place

import com.akkarimzai.task10.exceptions.BadRequestException
import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

class UpdatePlaceCommand(
    val name: String?,
    val address: String?,
    val description: String?,
    val subway: String?
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            var count = 0
            name?.let {
                validate(UpdatePlaceCommand::name)
                    .isNotBlank()
                    .hasSize(min = 3, max = 60)
                count++
            }

            address?.let {
                validate(UpdatePlaceCommand::address)
                    .isNotBlank()
                    .hasSize(min = 3, max = 256)
                count++
            }

            description?.let {
                validate(UpdatePlaceCommand::description)
                    .isNotBlank()
                    .hasSize(min = 3, max = 256)
                count++
            }

            subway?.let {
                validate(UpdatePlaceCommand::subway)
                    .isNotBlank()
                    .hasSize(min = 3, max = 60)
                count++
            }

            if (count == 0) {
                throw BadRequestException("Nothing to update.")
            }
        }
    }
}