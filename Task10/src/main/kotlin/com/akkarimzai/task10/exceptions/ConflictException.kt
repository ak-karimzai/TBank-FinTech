package com.akkarimzai.task10.exceptions

class ConflictException(
    key: String, value: Any
) : BaseApiException("$key: $value already exist")