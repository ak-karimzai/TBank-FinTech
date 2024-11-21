package com.akkarimzai.io

interface MessageBroker {
    fun runProducersAndConsumers(producerCount: Int, consumerCount: Int)
}