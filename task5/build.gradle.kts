import org.jetbrains.kotlin.backend.wasm.lower.excludeDeclarationsFromCodegen
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.distsDirectory

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("jacoco")
}

group = "com.akkarimzai"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.github.resilience4j:resilience4j-spring-boot2:2.2.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.valiktor:valiktor-core:0.12.0")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("io.kotest:kotest-runner-junit5:5.7.1")
    testImplementation("io.kotest:kotest-assertions-core:5.7.1")
    testImplementation("io.kotest:kotest-property:5.7.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
    testImplementation("io.mockk:mockk:1.13.11")

    testImplementation("org.testcontainers:junit-jupiter:1.20.1")
    implementation("org.wiremock.integrations.testcontainers:wiremock-testcontainers-module:1.0-alpha-14")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

jacoco {
    toolVersion = "0.8.12"
    reportsDirectory = layout.buildDirectory.dir("customJacocoReportDir")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<JacocoReport> {
    dependsOn(tasks.test)
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
    afterEvaluate {
        classDirectories = files(classDirectories.files.map { it ->
            fileTree(it).apply {
                exclude( "**/models**")
                exclude("**/entities**")
            }
        })
    }
}