package org.jetbrains.benchmarksLauncher

import consume1

fun assert(value: Boolean) { if (!value) error("Assertion failed") }

class Blackhole {
    companion object {
        fun consume(value: Any) {
            consume1(value)
        }
    }
}

class Random() {
    companion object {
        var seedInt: Int = 0
        fun nextInt(boundary: Int = 100): Int = (seedInt++) % boundary

        var seedDouble: Double = 0.0
        fun nextDouble(boundary: Double = 100.0): Double = seedDouble.also { seedDouble += 0.001 }
    }
}