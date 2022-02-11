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

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xwasm-launcher=d8",
        "-Xwasm-debug-info=false"  // Needed for binaryen
    )
}

val v8: String? by project
val v8path = v8 ?: (System.getProperty("user.home") + "/.jsvu/v8")

val wasmOpt: String? by project
val wasmOptPath = wasmOpt ?: (System.getProperty("user.home") + "/work/wasm/binaryen/bin/wasm-opt")

val v8flags = arrayOf(
    "--experimental-wasm-typed-funcref",
    "--experimental-wasm-gc",
    "--experimental-wasm-eh",
    "--wasm-opt",
)

val jsBench by tasks.registering(Exec::class) {
    dependsOn(":compileProductionExecutableKotlinJs")
    executable(v8path)
    setWorkingDir("$buildDir/js/packages/kotlin-wasm-benchmark/kotlin/")
    args(
        *v8flags,
        "./kotlin-kotlin-stdlib-js-ir.js",
        "./kotlin-wasm-benchmark.js"
    )
    doFirst {
        standardOutput = FileOutputStream("$buildDir/jsReport.json")
    }
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
    doFirst {
        standardOutput = FileOutputStream("$buildDir/wasmReport.json")
    }
}

val wasmCopyWasmForOptimizations by tasks.registering(Copy::class) {
    dependsOn(":compileProductionExecutableKotlinWasm")
    from("$buildDir/js/packages/kotlin-wasm-benchmark-wasm/")
    into("$buildDir/js/packages/kotlin-wasm-benchmark-wasm-opt/")
}

val runBinaryen by tasks.registering(Exec::class) {
    dependsOn(wasmCopyWasmForOptimizations)
    executable(wasmOptPath)
    setWorkingDir("$buildDir/js/packages/kotlin-wasm-benchmark-wasm-opt/kotlin/")
    args(
        "--enable-nontrapping-float-to-int",
        "--enable-typed-function-references",
        "--enable-gc",
        "--enable-reference-types",
        "--enable-exception-handling",
        "../../kotlin-wasm-benchmark-wasm/kotlin/kotlin-wasm-benchmark-wasm.wasm", "-o", "./kotlin-wasm-benchmark-wasm.wasm",
        "-O3",
        "--inline-functions-with-loops",
        "--traps-never-happen",
        "--fast-math",
    )
}

val wasmBenchOpt by tasks.registering(Exec::class) {
    dependsOn(runBinaryen)
    executable(v8path)
    setWorkingDir("$buildDir/js/packages/kotlin-wasm-benchmark-wasm-opt/kotlin/")
    args(
        *v8flags,
        "--module",
        "./kotlin-wasm-benchmark-wasm.js"
    )
    doFirst {
        standardOutput = FileOutputStream("$buildDir/wasmReportOpt.json")
    }
}
