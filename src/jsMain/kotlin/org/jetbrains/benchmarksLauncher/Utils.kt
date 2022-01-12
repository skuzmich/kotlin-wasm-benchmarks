package org.jetbrains.benchmarksLauncher

actual fun writeToFile(fileName: String, text: String) {
    println("Writing to file: $fileName, \n------\n$text\n------\n")
}

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

actual fun nanoTime(): Long {
    return (js("performance.now()") as Double * 1000000.0).toLong()
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
