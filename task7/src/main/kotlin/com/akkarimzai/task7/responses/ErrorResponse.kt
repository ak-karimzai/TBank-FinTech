package com.akkarimzai.task7.responses

class ErrorResponse<T>(
    val status: Int, val message: T
)