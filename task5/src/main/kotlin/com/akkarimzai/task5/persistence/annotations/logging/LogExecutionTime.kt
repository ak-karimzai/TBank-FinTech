package com.akkarimzai.task5.persistence.annotations.logging

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogExecutionTime