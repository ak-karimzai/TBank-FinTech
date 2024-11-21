package com.akkarimzai.benchmark

import com.akkarimzai.io.MessageBroker
import com.akkarimzai.io.RabbitMQBroker
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
open class BrokerBenchmark {
    private lateinit var broker: MessageBroker

    @Param(value = ["RabbitMQ", "Kafka"])
    var brokerType: String = "RabbitMQ"

    @Param(value = ["1", "3", "10"])
    var producerCount: Int = 1

    @Param(value = ["1", "3", "10"])
    var consumerCount: Int = 1

    @Setup(Level.Trial)
    fun setup() {
        broker = when (brokerType) {
            "RabbitMQ" -> RabbitMQBroker("testQueue")
            "Kafka" -> RabbitMQBroker("testTopic")
            else -> throw IllegalArgumentException("Unknown broker type")
        }
        broker.setup()
    }

    @Benchmark
    fun produceMessages() {
        broker.sendMessage("Test Message")
    }

    @Benchmark
    fun consumeMessages() {
        broker.receiveMessage()
    }

    @TearDown(Level.Trial)
    fun teardown() {
        broker.teardown()
    }
}