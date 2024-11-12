package com.akkarimzai.task10.models.user

import com.akkarimzai.task10.models.common.ValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class ResetPasswordCommand(
    val username: String,
    val password: String,
    val verificationCode: String
) : ValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(ResetPasswordCommand::username)
                .isNotBlank()
                .hasSize(min = 6, max = 60)

            validate(ResetPasswordCommand::password)
                .isNotBlank()
                .hasSize(min = 6, max = 60)

            validate(ResetPasswordCommand::verificationCode)
                .isNotBlank()
                .isEmail()
                .hasSize(min = 4, max = 60)
        }
    }
}