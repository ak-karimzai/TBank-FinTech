package com.akkarimzai.io

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

class RabbitMQBroker(private val queueName: String): MessageBroker {
    private lateinit var channel: Channel

    override fun setup() {
        val factory = ConnectionFactory().apply {
            host = "localhost"
            username = "user"
            password = "password"
        }
        val connection = factory.newConnection()
        channel = connection.createChannel()
        channel.queueDeclare(queueName, false, false, false, null)
    }

    override fun sendMessage(message: String) {
        channel.basicPublish("", queueName, null, message.toByteArray())
    }

    override fun receiveMessage(): String {
        val delivery = channel.basicGet(queueName, true)
        return delivery?.body?.let { String(it) } ?: ""
    }

    override fun teardown() {
        channel.close()
    }
}