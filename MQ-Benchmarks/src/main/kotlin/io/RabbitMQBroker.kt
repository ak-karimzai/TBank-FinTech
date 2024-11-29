package com.akkarimzai.io

import com.akkarimzai.utils.generateRandomString
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.*
import kotlin.random.Random

class RabbitMQBroker(private val queueName: String = "test_queue") : MessageBroker {
    private val factory = ConnectionFactory().apply {
        host = "localhost"
        port = 5672
        username = "user"
        password = "password"
    }

    override fun runBenchmarks(producerCount: Int, consumerCount: Int) {
        runBlocking {
            val producers = (1..producerCount).map { id ->
                launch { runProducer(id) }
            }
            val consumers = (1..consumerCount).map { id ->
                launch { runConsumer(id) }
            }
            producers.forEach { it.join() }
            consumers.forEach { it.join() }
        }
    }

    private suspend fun runProducer(id: Int) {
        withContext(Dispatchers.IO) {
            var connection: com.rabbitmq.client.Connection? = null
            var channel: com.rabbitmq.client.Channel? = null
            try {
                connection = factory.newConnection()
                channel = connection.createChannel()
                channel.queueDeclare(queueName, false, false, false, null)
                repeat(1000) { i ->
                    val message = generateRandomString(Random.nextInt(1024, 10240))
                    channel.basicPublish("", queueName, null, message.toByteArray())
//                    println(message)
                }
            } finally {
                channel?.close()
                connection?.close()
            }
        }
    }

    private suspend fun runConsumer(id: Int) {
        withContext(Dispatchers.IO) {
            var connection: com.rabbitmq.client.Connection? = null
            var channel: com.rabbitmq.client.Channel? = null
            try {
                connection = factory.newConnection()
                channel = connection.createChannel()
                channel.queueDeclare(queueName, false, false, false, null)
                val consumerTag = "Consumer $id"
                channel.basicConsume(queueName, true, { _, delivery ->
                    val message = String(delivery.body)
//                    println("$consumerTag $message")
                }) { _ -> }
            } finally {
                channel?.close()
                connection?.close()
            }
        }
    }
}