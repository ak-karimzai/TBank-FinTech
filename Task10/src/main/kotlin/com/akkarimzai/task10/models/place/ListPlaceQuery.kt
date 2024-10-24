package com.akkarimzai.task10.models.place

import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate

data class ListPlaceQuery(
    val page: Int,
    val size: Int,
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(ListPlaceQuery::page)
                .isGreaterThan(0)

            validate(ListPlaceQuery::size)
                .isGreaterThan(0)
        }
    }
}