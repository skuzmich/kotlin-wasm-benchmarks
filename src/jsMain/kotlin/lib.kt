

fun performanceNow(): Double = js("performance.now()") as Double
fun dontOptimize(x: Int): Int = js("eval")(x)
fun log(x: Any?) { js("console.log")(x.toString()) }

fun consume1(x: Any) { js("eval")(x) }

val platform = "JS"

val recommendedWarmup = 10