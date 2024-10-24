package com.akkarimzai.task10.exceptions

class ValidationException(val validationResult: List<String>
) : BaseApiException(null)