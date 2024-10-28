package com.akkarimzai.task10.models.event

import org.valiktor.validate

import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.functions.isGreaterThan

data class GetEventQuery(
    val eventId: Long,
    val placeId: Long
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(GetEventQuery::eventId)
                .isGreaterThan(0)

            validate(GetEventQuery::placeId)
                .isGreaterThan(0)
        }
    }
}