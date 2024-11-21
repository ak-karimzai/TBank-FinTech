plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "Mq-Benchmark"
include("src:main:resources")
findProject(":src:main:resources")?.name = "resources"
