package com.akkarimzai.task10.exceptions

class NotFoundException(key: String, value: Any) : BaseApiException("$key: $value not found")