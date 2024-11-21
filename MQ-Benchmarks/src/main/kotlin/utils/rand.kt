package com.akkarimzai.utils

fun generateRandomString(n: Int): String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..n)
        .map { charset.random() }
        .joinToString("")
}