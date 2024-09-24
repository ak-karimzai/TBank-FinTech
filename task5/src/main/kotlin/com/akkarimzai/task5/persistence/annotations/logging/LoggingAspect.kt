package com.akkarimzai.task5.persistence.annotations.logging

import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {
    private val logger = KotlinLogging.logger {}

    @Pointcut("@annotation(com.akkarimzai.task5.persistence.annotations.logging.LogExecutionTime)")
    fun logExecutionTimeMethod() {}

    @Pointcut("@within(com.akkarimzai.task5.persistence.annotations.logging.LogExecutionTime)")
    fun logExecutionTimeClass() {}

    @Around("logExecutionTimeMethod() || logExecutionTimeClass()")
    @Throws(Throwable::class)
    fun logExecutionTime(joinPoint: ProceedingJoinPoint) : Any? {
        val start = System.currentTimeMillis()
        val result = joinPoint.proceed()

        val executionTime = System.currentTimeMillis() - start
        logger.info {
            "Executed method: {${joinPoint.signature.name}} in class: {${joinPoint.target.javaClass.simpleName}} in ${executionTime}ms."
        }

        return result
    }
}