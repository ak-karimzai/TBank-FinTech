package com.akkarimzai.task10.models.user

import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class RegisterCommand(
    val username: String,
    val email: String,
    val password: String
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(RegisterCommand::username)
                .isNotBlank()
                .hasSize(min = 6, max = 60)

            validate(RegisterCommand::email)
                .isNotBlank()
                .isEmail()
                .hasSize(min = 6, max = 60)

            validate(RegisterCommand::password)
                .isNotBlank()
                .hasSize(min = 6, max = 60)
        }
    }
}