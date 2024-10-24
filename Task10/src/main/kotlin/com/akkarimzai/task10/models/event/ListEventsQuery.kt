package com.akkarimzai.task10.models.event

import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.functions.*
import org.valiktor.validate
import java.time.LocalDateTime

class ListEventsQuery(
    val page: Int,
    val size: Int,
    val name: String? = null,
    val fromDate: LocalDateTime? = null,
    val toDate: LocalDateTime? = null
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(ListEventsQuery::page)
                .isGreaterThan(0)

            validate(ListEventsQuery::size)
                .isGreaterThan(0)

            name?.let {
                validate(ListEventsQuery::name)
                    .isNotBlank()
                    .hasSize(min = 1, max = 64)
            }
        }
    }
}