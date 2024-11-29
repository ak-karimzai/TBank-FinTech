package com.akkarimzai.task10.services

import com.akkarimzai.task10.models.user.JwtAuthResponse
import com.akkarimzai.task10.models.user.LoginCommand
import com.akkarimzai.task10.models.user.RegisterCommand
import com.akkarimzai.task10.models.user.ResetPasswordCommand
import com.akkarimzai.task10.profiles.toUser
import mu.KotlinLogging
import org.apache.coyote.BadRequestException
import org.slf4j.MDC
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager
) : AbstractService() {
    private val logger = KotlinLogging.logger {}

    fun register(command: RegisterCommand): JwtAuthResponse {
        MDC.put("RequestRegisterUsername", command.username)
        logger.info { "Request register: ${command.username}" }

        validateRequest(command)

        val user = command.toUser(passwordEncoder)

        return try {
            logger.info { "Mapped user" }

            val createdUser = userService.create(user)
            val jwt = jwtService.generateToken(createdUser)

            JwtAuthResponse(jwt)
        } finally {
            MDC.clear()
        }
    }

    fun login(command: LoginCommand): JwtAuthResponse {
        logger.info { "Request login: ${command.username}" }

        validateRequest(command)

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
            command.username,
            command.password
        ))

        val user = userService
            .userDetailsService()
            .loadUserByUsername(command.username)
        val jwt = jwtService.generateToken(user, command.rememberMe)
        return JwtAuthResponse(jwt)
    }

    fun reset(command: ResetPasswordCommand): JwtAuthResponse {
        logger.info { "Request reset: ${command.username}" }

        validateRequest(command)

        if (command.verificationCode != "0000") {
            throw BadRequestException("Invalid verification code!")
        }

        val user = userService
            .loadUserByUsername(command.username)
        user.password = passwordEncoder.encode(command.password)
        val savedUser = userService.save(user)
        val jwt = jwtService.generateToken(savedUser)

        return JwtAuthResponse(jwt)
    }

    fun logout(tokenHeader: String) {
        if (!tokenHeader.startsWith(BEARER_PREFIX)) {
            throw BadRequestException("User not authenticated!")
        }

        val token = tokenHeader.substring(BEARER_PREFIX.length)
        if (token.isEmpty()) {
            throw BadRequestException("Invalid authentication details")
        }

        jwtService.logout(token)
    }

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }
}