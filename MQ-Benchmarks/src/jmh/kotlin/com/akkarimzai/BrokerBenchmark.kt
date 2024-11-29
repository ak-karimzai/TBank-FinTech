package com.akkarimzai

import com.akkarimzai.io.KafkaBroker
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
open class BrokerBenchmark {
    @Param(value = ["1", "3"])
    var producerCount: Int = 1
    @Param(value = ["1", "3"])
    var consumerCount: Int = 1

    @Benchmark
    fun runBenchmark() {
        val broker = KafkaBroker()
        broker.runBenchmarks(producerCount, consumerCount)
    }
}