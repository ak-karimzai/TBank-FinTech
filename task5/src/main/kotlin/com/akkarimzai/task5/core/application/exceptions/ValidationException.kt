package com.akkarimzai.task5.core.application.exceptions

class ValidationException(val validationResult: List<String>) : BaseApiException()