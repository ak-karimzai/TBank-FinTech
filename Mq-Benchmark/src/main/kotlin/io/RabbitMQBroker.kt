package com.akkarimzai.io

import com.akkarimzai.utils.generateRandomString
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.random.Random

class RabbitMQBroker(private val queueName: String = "test_queue"): MessageBroker {
    val factory = ConnectionFactory().apply {
        host = "localhost"
        port = 5672
        username = "user"
        password = "password"
    }

    override fun runProducersAndConsumers(producerCount: Int, consumerCount: Int) {
        runBlocking {
            val producers = (1..producerCount).map {
                launch { runProducer(it) }
            }
            val consumers = (1..consumerCount).map {
                launch { runConsumer(it) }
            }
            producers.forEach { it.join() }
            consumers.forEach { it.join() }
        }
    }

    private suspend fun runProducer(id: Int) {
        withContext(Dispatchers.IO) {
            val connection = factory.newConnection()
            val channel = connection.createChannel()
            channel.queueDeclare(queueName, false, false, false, null)
            repeat(100) { i ->
                val message = "Producer $id - Message $i"
                channel.basicPublish("", queueName, null, message.toByteArray())
                println("Sent: $message")
            }
            channel.close()
            connection.close()
        }
    }

    private suspend fun runConsumer(id: Int) {
        withContext(Dispatchers.IO) {
            val connection = factory.newConnection()
            val channel = connection.createChannel()
            channel.queueDeclare(queueName, false, false, false, null)
            val consumerTag = "Consumer $id"
            channel.basicConsume(queueName, true, { _, delivery ->
                val message = String(delivery.body)
                println("$consumerTag received: $message")
            }) { _ -> }
        }
    }
}