package com.akkarimzai.task10.models.place

import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.validate

data class ListPlaceQuery(
    val page: Int,
    val size: Int,
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(ListPlaceQuery::page)
                .isGreaterThanOrEqualTo(0)

            validate(ListPlaceQuery::size)
                .isGreaterThan(0)
        }
    }
}