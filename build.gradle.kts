import java.io.FileOutputStream

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    js(IR) {
        nodejs()
        binaries.executable()
    }
    wasm {
        nodejs()
        binaries.executable()
    }
}

val v8: String? by project
val v8path = v8 ?: (System.getProperty("user.home") + "/.jsvu/v8")

val v8flags = arrayOf(
    "--experimental-wasm-typed-funcref",
    "--experimental-wasm-gc",
    "--wasm-opt",
)

val jsBench by tasks.registering(Exec::class) {
    dependsOn(":compileProductionExecutableKotlinJs")
    executable(v8path)
    setWorkingDir("$buildDir/js/packages/kotlin-wasm-benchmark/kotlin/")
    args(
        *v8flags,
        "./kotlin_kotlin.js",
        "./kotlin-wasm-benchmark.js"
    )
    standardOutput = FileOutputStream("$buildDir/jsReport.json")
}

val wasmBench by tasks.registering(Exec::class) {
    dependsOn(":compileProductionExecutableKotlinWasm")
    executable(v8path)
    setWorkingDir("$buildDir/js/packages/kotlin-wasm-benchmark-wasm/kotlin/")
    args(
        *v8flags,
        "--module",
        "./kotlin-wasm-benchmark-wasm.js"
    )
    standardOutput = FileOutputStream("$buildDir/wasmReport.json")
}