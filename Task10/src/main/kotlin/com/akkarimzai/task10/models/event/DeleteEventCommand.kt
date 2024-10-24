package com.akkarimzai.task10.models.event

import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate

class DeleteEventCommand(
    val placeId: Long,
    val eventId: Long
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(DeleteEventCommand::eventId)
                .isGreaterThan(0)

            validate(DeleteEventCommand::placeId)
                .isGreaterThan(0)
        }
    }
}