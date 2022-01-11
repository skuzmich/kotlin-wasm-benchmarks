pluginManagement {
    plugins {
        kotlin("multiplatform") version "1.6.255-SNAPSHOT"
    }
    resolutionStrategy {
    }
    repositories {
        mavenLocal()
        mavenCentral()
    }
}
rootProject.name = "kotlin-wasm-benchmark"