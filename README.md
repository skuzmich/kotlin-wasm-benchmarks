Microbenchmarks for experimental WasmGC-based Kotlin/Wasm compiler running in V8 against Kotlin/JS.

Adapted from [Kotlin/Native project](https://github.com/JetBrains/kotlin-native/tree/master/performance/ring/src/main/kotlin/org/jetbrains/ring)

# Run

1. Build Kotlin from sources 
   1. Clone https://github.com/JetBrains/kotlin
   2. [Set up JDKs and variables](https://github.com/JetBrains/kotlin#build-environment-requirements)
   3. Run `./gradlew install --parallel` to publish it to `~/.m2`

2. Get d8 shell. You can use  [JSVU](https://github.com/GoogleChromeLabs/jsvu) or [build it from sources]( https://v8.dev/docs/build)

3. Run `./gradlew bench -Pv8=/path/to/d8`

