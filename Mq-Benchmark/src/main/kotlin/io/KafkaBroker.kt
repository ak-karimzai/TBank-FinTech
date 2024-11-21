package com.akkarimzai.io

import com.akkarimzai.utils.generateRandomString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.time.Duration
import java.util.Properties
import kotlin.random.Random

class KafkaBroker(private val topic: String = "test_topic"): MessageBroker {
    private val producerProps = Properties().apply {
        put("bootstrap.servers", "localhost:9092")
        put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    }

    private val consumerProps = Properties().apply {
        put("bootstrap.servers", "localhost:9092")
        put("group.id", "test_group")
        put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
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
            KafkaProducer<String, String>(producerProps).use { producer ->
                repeat(100) { i ->
                    val message = "Producer $id - Message $i"
                    producer.send(ProducerRecord(topic, null, message))
                }
            }
        }
    }

    private suspend fun runConsumer(id: Int) {
        withContext(Dispatchers.IO) {
            KafkaConsumer<String, String>(consumerProps).use { consumer ->
                consumer.subscribe(listOf(topic))
                while (true) {
                    val records = consumer.poll(Duration.ofMillis(100))
                }
            }
        }
    }
}