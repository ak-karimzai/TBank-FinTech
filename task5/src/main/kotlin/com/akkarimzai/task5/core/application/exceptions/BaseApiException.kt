package com.akkarimzai.task5.core.application.exceptions

open class BaseApiException : Exception {
    constructor(message: String) : super(message)
    constructor() : super()
}