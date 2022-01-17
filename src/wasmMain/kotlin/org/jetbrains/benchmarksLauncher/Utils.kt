package org.jetbrains.benchmarksLauncher

actual fun assert(value: Boolean) {
    if (!value) throw AssertionError()
}

@JsFun("performance.now")
external fun performanceNow(): Double

@JsFun("Date")
external fun jsDate(): String

actual inline fun measureNanoTime(block: () -> Unit): Long {
    val timeBefore = performanceNow()
    block()
    val timeAfter = performanceNow()
    return ((timeAfter - timeBefore) * 1_000_000.0).toLong()
}

actual fun cleanup() {
}

actual fun printStderr(message: String) {
    print("ERR: $message")
}

actual fun currentTime(): String {
    return jsDate()
}