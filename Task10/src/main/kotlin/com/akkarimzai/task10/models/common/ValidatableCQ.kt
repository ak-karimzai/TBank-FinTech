package com.akkarimzai.task10.models.common

import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage

abstract class ValidatableCQ {
    fun validate() : List<String> {
        var result: List<String> = listOf()
        try {
            dataValidator()
        } catch (e: ConstraintViolationException) {
            result = e.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }
        }
        return result
    }

    abstract fun dataValidator()
}