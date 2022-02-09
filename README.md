Microbenchmarks for experimental WasmGC-based Kotlin/Wasm compiler running in V8 against Kotlin/JS.

Adapted from [Kotlin/Native project](https://github.com/JetBrains/kotlin-native/tree/master/performance/ring/src/main/kotlin/org/jetbrains/ring)

# Run

1. Build Kotlin from sources 
   1. Clone https://github.com/JetBrains/kotlin
   2. [Set up JDKs and variables](https://github.com/JetBrains/kotlin#build-environment-requirements)
   3. Run `./gradlew install --parallel` to publish it to `~/.m2`

2. Get d8 shell. You can use  [JSVU](https://github.com/GoogleChromeLabs/jsvu) or [build it from sources]( https://v8.dev/docs/build)

3. Get wasm-opt from https://github.com/WebAssembly/binaryen
4. Run `./gradlew jsBench wasmBench wasmBenchOpt -Pv8=/path/to/d8 -PwasmOpt=/path/to/wasm-opt`
5. Compare ./build/*.json results using [K/N benchmarks analyzer](https://github.com/JetBrains/kotlin/blob/0bd4dbc0c16899ec5a554895af57bd3a80cae760/kotlin-native/HACKING.md#performance-measurement) with flag -f

