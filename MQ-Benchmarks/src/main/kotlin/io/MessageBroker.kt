package com.akkarimzai.io

interface MessageBroker {
    fun runBenchmarks(producerCount: Int, consumerCount: Int)
}