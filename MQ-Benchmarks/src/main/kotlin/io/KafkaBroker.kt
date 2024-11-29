package com.akkarimzai.io

import com.akkarimzai.utils.generateRandomString
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.apache.kafka.clients.consumer.ConsumerConfig

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.time.Duration
import java.util.Properties
import kotlin.random.Random

class KafkaBroker(private val topic: String = "test_topic"): MessageBroker {
    private val producerProps = Properties().apply {
        put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
    }

    private val consumerProps = Properties().apply {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        put(ConsumerConfig.GROUP_ID_CONFIG, "test_group")
        put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    }

    override fun runBenchmarks(producerCount: Int, consumerCount: Int) {
        runBlocking {
            val producers = (1..producerCount).map { id ->
                launch { runProducer() }
            }

            val consumers = (1..consumerCount).map { id ->
                launch { runConsumer() }
            }

            producers.forEach { it.join() }
            consumers.forEach { it.join() }
        }
    }

    private suspend fun runProducer() {
        val producer = KafkaProducer<String, String>(producerProps)
        val message = generateRandomString(Random.nextInt(1024, 10240))
        producer.use { producer ->
            val record = ProducerRecord(topic, "key", message)
            producer.send(record) { metadata, exception ->
            }
            delay(100)
        }
    }

    private suspend fun runConsumer() {
        withContext(Dispatchers.IO) {
            val consumer = KafkaConsumer<String, String>(consumerProps)
            consumer.subscribe(listOf(topic))
            consumer.use { consumer ->
                while (true) {
                    val records = consumer.poll(Duration.ofMillis(1000))
                    for (record in records) {
//                        println("Consumer  received message: ${record.value()}")
                    }
                }
            }
        }
    }
}