package com.akkarimzai.io

interface MessageBroker {
    fun setup()
    fun sendMessage(message: String)
    fun receiveMessage(): String
    fun teardown()
}