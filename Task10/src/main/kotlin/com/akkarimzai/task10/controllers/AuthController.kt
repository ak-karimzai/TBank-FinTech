package com.akkarimzai.task10.controllers

import com.akkarimzai.task10.models.user.JwtAuthResponse
import com.akkarimzai.task10.models.user.LoginCommand
import com.akkarimzai.task10.models.user.RegisterCommand
import com.akkarimzai.task10.models.user.ResetPasswordCommand
import com.akkarimzai.task10.services.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody command: RegisterCommand): JwtAuthResponse {
        return authenticationService.register(command)
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.CREATED)
    fun login(@RequestBody command: LoginCommand): JwtAuthResponse {
        return authenticationService.login(command)
    }

    @PostMapping("logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@RequestHeader("Authorization") tokenHeader: String) {
        return authenticationService.logout(tokenHeader)
    }

    @PostMapping("reset")
    @ResponseStatus(HttpStatus.CREATED)
    fun reset(@RequestBody command: ResetPasswordCommand): JwtAuthResponse {
        return authenticationService.reset(command)
    }
}