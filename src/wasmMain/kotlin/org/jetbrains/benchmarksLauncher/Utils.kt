package org.jetbrains.benchmarksLauncher

actual fun writeToFile(fileName: String, text: String) {
    println("Writing to file: $fileName, \n------\n$text\n------\n")
}

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

actual fun nanoTime(): Long {
    return (performanceNow() * 1_000_000.0).toLong()
}

actual class Blackhole {
    actual companion object {
        actual var consumer = 0
        actual fun consume(value: Any) {
            consumer += value.hashCode()
        }
    }
}

actual class Random actual constructor() {
    actual companion object {
        actual var seedInt = 0
        actual fun nextInt(boundary: Int): Int {
            seedInt = (3 * seedInt + 11) % boundary
            return seedInt
        }

        actual var seedDouble: Double = 0.1
        actual fun nextDouble(boundary: Double): Double {
            seedDouble = (7.0 * seedDouble + 7.0) % boundary
            return seedDouble
        }
    }
}
