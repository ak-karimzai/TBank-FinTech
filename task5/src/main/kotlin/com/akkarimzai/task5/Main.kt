package com.akkarimzai.task5

import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import sun.misc.Signal
import sun.misc.SignalHandler


@SpringBootApplication
class Main : CommandLineRunner {
    val logger = KotlinLogging.logger {}

    override fun run(vararg args: String?) {
        logger.info { "Server started" }

        Signal.handle(Signal("TERM"), SignalHandler {
            logger.info {  "Received SIGTERM signal. Shutting down..." }
            shutdownGracefully()
        })
    }

    private fun shutdownGracefully() {
        context.close().also {
            logger.warn { "Server stopped gracefully" }
        }
    }

    companion object {
        lateinit var context: ConfigurableApplicationContext
    }
}

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}