package com.akkarimzai.task5.core.application.models

import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isLessThan
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.validate

data class PageableList(
    val page: Int = 1,
    val size: Int = 10
) : ValidatableDto() {
    override fun dataValidator() {
        validate(this) {
            validate(PageableList::page)
                .isGreaterThan(0)

            validate(PageableList::size)
                .isGreaterThan(0)
                .isLessThanOrEqualTo(20)
        }
    }
}