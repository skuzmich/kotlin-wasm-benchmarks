package org.jetbrains.benchmarksLauncher

actual fun assert(value: Boolean) {
    if (!value) throw AssertionError()
}

actual inline fun measureNanoTime(block: () -> Unit): Long {
    val timeBefore = js("performance.now()") as Double
    block()
    val timeAfter = js("performance.now()") as Double
    return ((timeAfter - timeBefore) * 1000000.0).toLong()
}

actual fun cleanup() {
}

actual fun printStderr(message: String) {
    print("ERR: $message")
}

actual fun currentTime(): String {
    return js("Date()") as String
}
