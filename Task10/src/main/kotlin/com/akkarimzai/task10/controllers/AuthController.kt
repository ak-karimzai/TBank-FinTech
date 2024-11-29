package com.akkarimzai.task10.controllers

import com.akkarimzai.task10.models.user.JwtAuthResponse
import com.akkarimzai.task10.models.user.LoginCommand
import com.akkarimzai.task10.models.user.RegisterCommand
import com.akkarimzai.task10.models.user.ResetPasswordCommand
import com.akkarimzai.task10.services.AuthenticationService
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService,
    private val registry: MeterRegistry
//    private val passwordEncoder: PasswordEncoder
) {
    private var counter: Counter = registry.counter("auth_requests")

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody command: RegisterCommand): JwtAuthResponse {
        counter.increment()
        return authenticationService.register(command)
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.CREATED)
    fun login(@RequestBody command: LoginCommand): JwtAuthResponse {
        counter.increment()
        return authenticationService.login(command)
    }

    @PostMapping("logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@RequestHeader("Authorization") tokenHeader: String) {
        counter.increment()
        return authenticationService.logout(tokenHeader)
    }

    @PostMapping("reset")
    @ResponseStatus(HttpStatus.CREATED)
    fun reset(@RequestBody command: ResetPasswordCommand): JwtAuthResponse {
        counter.increment()
        return authenticationService.reset(command)
    }
}