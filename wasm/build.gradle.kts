plugins {
    id("org.jetbrains.kotlin.js")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-wasm"))
}

kotlin {
    js(IR) {
        nodejs {
        }
        binaries.executable()
    }

    sourceSets["main"].apply {
        kotlin.srcDirs("../src", "../srcWasm")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>() {
    kotlinOptions {
        freeCompilerArgs += listOf("-Xwasm")
    }
}