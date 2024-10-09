package com.akkarimzai.task7

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
class Task7Application

fun main(args: Array<String>) {
    runApplication<Task7Application>(*args)
}
