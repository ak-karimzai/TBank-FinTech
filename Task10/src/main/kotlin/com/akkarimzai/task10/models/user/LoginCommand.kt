package com.akkarimzai.task10.models.user

import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class LoginCommand(
    val username: String,
    val password: String,
    val rememberMe: Boolean = false
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(LoginCommand::username)
                .isNotBlank()
                .hasSize(min = 6, max = 60)

            validate(LoginCommand::password)
                .isNotBlank()
                .hasSize(min = 6, max = 60)
        }
    }
}