val platform = "Wasm"

@JsFun("performance.now")
external fun performanceNow(): Double

@JsFun("(x) => x")
external fun dontOptimize(x: Int): Int

@JsFun("console.log")
private external fun logImpl(x: String): Unit

fun log(x: Any?) = logImpl(x.toString())

@JsFun("function(x){}")
fun consume1(x: Any) { }

val recommendedWarmup = 5