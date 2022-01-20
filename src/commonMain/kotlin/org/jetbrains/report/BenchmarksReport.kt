/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

package org.jetbrains.report

class BenchmarkResult(
    val name: String,
    val status: Status,
    val score: Double,
    val metric: Metric,
    val runtimeInUs: Double,
    val repeat: Int,
    val warmup: Int
) {
    enum class Metric(val suffix: String, val value: String) {
        EXECUTION_TIME("", "EXECUTION_TIME"),
        CODE_SIZE(".codeSize", "CODE_SIZE"),
        COMPILE_TIME(".compileTime", "COMPILE_TIME"),
        BUNDLE_SIZE(".bundleSize", "BUNDLE_SIZE")
    }

    enum class Status(val value: String) {
        PASSED("PASSED"),
        FAILED("FAILED")
    }

    fun toJson(): String {
        return """
        {
            "name": "${name.removeSuffix(metric.suffix)}",
            "status": "${status.value}",
            "score": ${score},
            "metric": "${metric.value}",
            "runtimeInUs": ${runtimeInUs},
            "repeat": ${repeat},
            "warmup": ${warmup}
        }
        """
    }
}