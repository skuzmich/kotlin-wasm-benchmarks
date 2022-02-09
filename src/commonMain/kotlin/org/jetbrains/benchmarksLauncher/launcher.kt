/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.benchmarksLauncher

import org.jetbrains.report.BenchmarkResult

data class RecordTimeMeasurement(
    val status: BenchmarkResult.Status,
    val iteration: Int,
    val warmupCount: Int,
    val durationNs: Double
)

abstract class Launcher {
    abstract val benchmarks: BenchmarksCollection

    fun add(name: String, benchmark: AbstractBenchmarkEntry) {
        benchmarks[name] = benchmark
    }

    fun runBenchmark(benchmarkInstance: Any?, benchmark: AbstractBenchmarkEntry, repeatNumber: Int): Long {
        var i = repeatNumber
        return if (benchmark is BenchmarkEntryWithInit) {
            cleanup()
            measureNanoTime {
                while (i-- > 0) benchmark.lambda(benchmarkInstance!!)
                cleanup()
            }
        } else if (benchmark is BenchmarkEntry) {
            cleanup()
            measureNanoTime {
                while (i-- > 0) {
                    benchmark.lambda()
                }
                cleanup()
            }
        } else if (benchmark is BenchmarkEntryManual) {
            error("runBenchmark cannot run manual benchmark")
        } else {
            error("Unknown benchmark type $benchmark")
        }
    }

    enum class LogLevel { DEBUG, OFF }

    class Logger(val level: LogLevel = LogLevel.OFF) {
         fun log(message: String, messageLevel: LogLevel = LogLevel.DEBUG, usePrefix: Boolean = true) {
            if (messageLevel == level) {
                if (usePrefix) {
                    printStderr("[$level][${currentTime()}] $message")
                } else {
                    printStderr("$message")
                }
            }
        }
    }

    fun runBenchmark(logger: Logger,
                     numWarmIterations: Int,
                     numberOfAttempts: Int,
                     name: String,
                     recordMeasurement: (RecordTimeMeasurement) -> Unit,
                     benchmark: AbstractBenchmarkEntry) {
        val benchmarkInstance = (benchmark as? BenchmarkEntryWithInit)?.ctor?.invoke()
        logger.log("Warm up iterations for benchmark $name\n")
        runBenchmark(benchmarkInstance, benchmark, numWarmIterations)
        val expectedDuration = /* ZZ: 1000L * */ 1_000_000 // 1s
        var autoEvaluatedNumberOfMeasureIteration = 1
        if (benchmark.useAutoEvaluatedNumberOfMeasure) {
            val time = runBenchmark(benchmarkInstance, benchmark, 1)
            if (time == 0L) {
                autoEvaluatedNumberOfMeasureIteration = 10_000_000
            } else if (time < expectedDuration)
                // Made auto evaluated number of measurements to be a multiple of 4.
                // Loops which iteration number is a multiple of 4 execute optimally,
                // because of different optimizations on processor (e.g. LSD)
                autoEvaluatedNumberOfMeasureIteration = ((expectedDuration / time).toInt() / 4 + 1) * 4
        }
        logger.log("Running benchmark $name ")
        for (k in 0.until(numberOfAttempts)) {
            logger.log(".", usePrefix = false)
            var i = autoEvaluatedNumberOfMeasureIteration
            val time = runBenchmark(benchmarkInstance, benchmark, i)
            val scaledTime = time * 1.0 / autoEvaluatedNumberOfMeasureIteration
            // Save benchmark object
            recordMeasurement(RecordTimeMeasurement(BenchmarkResult.Status.PASSED, k, numWarmIterations, scaledTime))
        }
        logger.log("\n", usePrefix = false)
    }

    fun launch(numWarmIterations: Int,
               numberOfAttempts: Int,
               prefix: String = "",
               filters: Collection<String>? = null,
               filterRegexes: Collection<String>? = null,
               verbose: Boolean): List<BenchmarkResult> {
        val logger = if (verbose) Logger(LogLevel.DEBUG) else Logger()
        val regexes = filterRegexes?.map { it.toRegex() } ?: listOf()
        val filterSet = filters?.toHashSet() ?: hashSetOf()
        // Filter benchmarks using given filters, or run all benchmarks if none were given.
        val runningBenchmarks = if (filterSet.isNotEmpty() || regexes.isNotEmpty()) {
            benchmarks.filterKeys { benchmark -> benchmark in filterSet || regexes.any { it.matches(benchmark) } }
        } else benchmarks
        if (runningBenchmarks.isEmpty()) {
            printStderr("No matching benchmarks found\n")
            error("No matching benchmarks found")
        }
        val benchmarkResults = mutableListOf<BenchmarkResult>()
        for ((name, benchmark) in runningBenchmarks) {
            val recordMeasurement : (RecordTimeMeasurement) -> Unit = {
                benchmarkResults.add(BenchmarkResult(
                    "$prefix$name",
                    it.status,
                    it.durationNs / 1000,
                    BenchmarkResult.Metric.EXECUTION_TIME,
                    it.durationNs / 1000,
                    it.iteration + 1,
                    it.warmupCount))
            }
            try {
                runBenchmark(logger, numWarmIterations, numberOfAttempts, name, recordMeasurement, benchmark)
            } catch (e: Throwable) {
                printStderr("Failure while running benchmark $name: $e\n")
                benchmarkResults.add(BenchmarkResult(
                        "$prefix$name", BenchmarkResult.Status.FAILED, 0.0,
                        BenchmarkResult.Metric.EXECUTION_TIME, 0.0, numberOfAttempts, numWarmIterations)
                )
                throw e
            }
        }
        return benchmarkResults
    }

    fun benchmarksListAction() {
        benchmarks.keys.forEach {
            println(it)
        }
    }
}

abstract class BenchmarkArguments()

class BaseBenchmarkArguments(): BenchmarkArguments() {
    val warmup: Int = 5
    val repeat: Int = 5
    val filter: List<String> = emptyList()
    val filterRegex: List<String> = emptyList()
    val verbose: Boolean = false
}

object BenchmarksRunner {
    fun parse(args: Array<String>, benchmarksListAction: ()->Unit): BenchmarkArguments? {
        return BaseBenchmarkArguments()
    }

    fun collect(results: List<BenchmarkResult>, arguments: BenchmarkArguments) {
        if (arguments is BaseBenchmarkArguments) {
            JsonReportCreator(results).printJsonReport()
        }
    }

    fun runBenchmarks(args: Array<String>,
                      run: (parser: BenchmarkArguments) -> List<BenchmarkResult>,
                      parseArgs: (args: Array<String>, benchmarksListAction: ()->Unit) -> BenchmarkArguments? = this::parse,
                      collect: (results: List<BenchmarkResult>, arguments: BenchmarkArguments) -> Unit = this::collect,
                      benchmarksListAction: ()->Unit) {
        val arguments = parseArgs(args, benchmarksListAction)
        arguments?.let {
            val results = run(arguments)
            collect(results, arguments)
        }
    }
}
