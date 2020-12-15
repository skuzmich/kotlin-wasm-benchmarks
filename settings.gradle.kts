pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.js") version "1.4.255-SNAPSHOT"
    }
    resolutionStrategy {
    }
    repositories {
        mavenLocal()
        mavenCentral()
    }
}
rootProject.name = "kotlin-wasm-benchmark"

include("js")
include("wasm")