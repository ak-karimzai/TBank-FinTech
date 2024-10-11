package com.akkarimzai.task7.exceptions

class NotFoundException(
    key: String, value: Any
) : Exception("${key}: $value not found.");