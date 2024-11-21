package com.akkarimzai

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

import com.akkarimzai.io.RabbitMQBroker

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
open class BrokerBenchmark {
    @Benchmark
    fun rabbitMqProducerBenchmark() {
        val broker = RabbitMQBroker()
        broker.runProducersAndConsumers(1, 1)
    }
}