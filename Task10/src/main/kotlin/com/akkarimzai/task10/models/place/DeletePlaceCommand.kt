package com.akkarimzai.task10.models.place

import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate

class DeletePlaceCommand(
    val placeId: Long
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(DeletePlaceCommand::placeId)
                .isGreaterThan(0)
        }
    }
}