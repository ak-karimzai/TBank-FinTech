package com.akkarimzai

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit
import java.util.*

import com.akkarimzai.io.RabbitMQBroker

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
open class MessagingBenchmark {
    @Benchmark
    fun rabbitMqProducerBenchmark() {
        val broker = RabbitMQBroker()
        broker.runProducersAndConsumers(1, 1)
    }
}