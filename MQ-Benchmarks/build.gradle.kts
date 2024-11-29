plugins {
    kotlin("jvm") version "2.0.0"
    id("me.champeau.jmh") version "0.7.2"
}

group = "com.akkarimzai"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.rabbitmq:amqp-client:5.23.0")
    implementation("org.apache.kafka:kafka-clients:3.9.0")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("org.openjdk.jmh:jmh-core:1.37")
    jmhImplementation("org.openjdk.jmh:jmh-core:1.37")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    testImplementation(kotlin("test"))
}

jmh {
    duplicateClassesStrategy = DuplicatesStrategy.WARN
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}