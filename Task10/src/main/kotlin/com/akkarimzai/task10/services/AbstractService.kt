package com.akkarimzai.task10.services

import com.akkarimzai.task10.exceptions.ValidationException
import com.akkarimzai.task10.models.common.ValidatableCQ

abstract class AbstractService {
    protected fun validateRequest(request: ValidatableCQ) {
        val validationResult = request.validate()
        validationResult.let {
            if (it.isNotEmpty()) {
                throw ValidationException(it)
            }
        }
    }
}