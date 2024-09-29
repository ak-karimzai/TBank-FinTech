package com.akkarimzai.task5.core.application.exceptions

class NotFoundException(private val key: String, private val value: Any) : BaseApiException("$key: $value not found")