package com.akkarimzai.task10.responses

class ErrorResponse<T>(
    val status: Int, val message: T
)