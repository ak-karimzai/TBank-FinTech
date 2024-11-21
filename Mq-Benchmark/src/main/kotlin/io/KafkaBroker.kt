package com.akkarimzai.io

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Properties

class KafkaBroker(private val topic: String): MessageBroker {
    private lateinit var producer: KafkaProducer<String, String>
    private lateinit var consumer: KafkaConsumer<String, String>

    override fun setup() {
        val producerProps = Properties().apply {
            put("bootstrap.servers", "localhost:9092")
            put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
            put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        }
        producer = KafkaProducer(producerProps)

        val consumerProps = Properties().apply {
            put("bootstrap.servers", "localhost:9092")
            put("group.id", "benchmark")
            put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
            put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        }
        consumer = KafkaConsumer(consumerProps)
        consumer.subscribe(listOf(topic))
    }

    override fun sendMessage(message: String) {
        val record = ProducerRecord(topic, "key", message)
        producer.send(record)
    }

    override fun receiveMessage(): String {
        val records = consumer.poll(100)
        return records.firstOrNull()?.value() ?: ""
    }

    override fun teardown() {
        producer.close()
        consumer.close()
    }
}