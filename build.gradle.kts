import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sqrt
import java.io.FileOutputStream

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    js(IR) {
        nodejs()
        binaries.executable()
    }
    wasm {
        nodejs()
        binaries.executable()
    }
}

val v8: String? by project
val v8path = v8 ?: (System.getProperty("user.home") + "/.jsvu/v8")

val jsBenchDataPath = "$buildDir/js_bench_data.txt"
val jsBench by tasks.registering(Exec::class) {
    dependsOn(":compileProductionExecutableKotlinJs")
    executable(v8path)
    setWorkingDir("$buildDir/js/packages/kotlin-wasm-benchmark/kotlin/")
    args(
        "--experimental-wasm-typed-funcref",
        "--experimental-wasm-gc",
        "./kotlin_kotlin.js",
        "./kotlin-wasm-benchmark.js"
    )
    doFirst {
        standardOutput = FileOutputStream("$buildDir/jsReport2.json")
    }
}

val wasmBenchDataPath = "$buildDir/wasm_bench_data.txt"
val wasmBench by tasks.registering(Exec::class) {
    dependsOn(":compileProductionExecutableKotlinWasm")
    executable(v8path)
    setWorkingDir("$buildDir/js/packages/kotlin-wasm-benchmark-wasm/kotlin/")
    args(
        "--experimental-wasm-typed-funcref",
        "--experimental-wasm-gc",
        "--wasm-opt",
        "--module",
        "./kotlin-wasm-benchmark-wasm.js"
    )

    doFirst {
        standardOutput = FileOutputStream("$buildDir/wasmReport2.json")
    }
}

class Table(
    val numRows: Int,
    val numColumns: Int,
    val data: List<List<Any?>>
) {
    override fun toString(): String {
        val columnSizes: MutableList<Int> = MutableList(numColumns) { col ->
            data.map { it[col].toString().length }.maxOrNull() ?: 0
        }

        return buildString {
            for ((idx, row) in data.withIndex()) {
                if (idx == 1) {
                    append("|")
                    for (size in columnSizes) {
                        repeat(size + 2) { append('-') }
                        append("|")
                    }
                    appendLine()
                }
                append("| ")
                for ((el, size) in row.zip(columnSizes)) {
                    append(el.toString().padStart(size, ' '))
                    append(" | ")
                }
                appendLine()
            }
        }
    }
}

fun compareResults(
    name1: String,
    name2: String,
    times1: Map<String, List<Double>>,
    times2: Map<String, List<Double>>
): Table {
    require(times1.keys == times1.keys) {
        "Measurements must have the same set of benchmarks"
    }

    val benches = times1.keys.sortedBy {
        times1[it]!!.average() / times2[it]!!.average()
    }

    val numRows = benches.size + 2
    val numColumns = 6
    val data = MutableList(numRows) { MutableList<Any?>(numColumns) { "" } }

    val header = data[0]

    header[0] = "Benchmark"
    header[1] = "Ratio $name1 / $name2"
    header[2] = name1
    header[3] = "±"
    header[4] = name2
    header[5] = "±"


    fun List<Double>.avg(): String = average().format()
    fun List<Double>.stddev(): String = "± " + ((standardDeviation() * 100.0) / average()).format() + "%"

    val ratios = mutableListOf<Double>()
    for ((index, bench) in benches.withIndex()) {
        val row = data[index + 1]
        val t1 = times1[bench]!!
        val t2 = times2[bench]!!

        ratios += t1.average() / t2.average()

        row[0] = bench
        row[1] = (t1.average() / t2.average()).format()
        row[2] = t1.avg()
        row[3] = t1.stddev()
        row[4] = t2.avg()
        row[5] = t2.stddev()
    }


    val footer = data.last()
    footer[0] = "Geomean"
    footer[1] = ratios.geometricMean().format()

    return Table(numRows, numColumns, data)
}

val bench by tasks.registering {
    dependsOn(wasmBench, jsBench)

    fun times(path: String): Map<String, List<Double>> =
        File(path).readLines().filter {
            it.startsWith("Bench")
        }.map {
            val (_, _, name, time) = it.split(":")
            Pair(name, time.toDouble())
        }.groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        ).mapValues {
            it.value
        }

    doLast {
        val timesJs: Map<String, List<Double>> = times(jsBenchDataPath)
        val timesWasm: Map<String, List<Double>> = times(wasmBenchDataPath)

        val table = compareResults("Wasm", "JS", timesWasm, timesJs)
        println(table)
    }
}

fun List<Double>.standardDeviation(): Double {
    val average = average()
    return sqrt(sumByDouble { (it - average).pow(2) } / (size - 1))
}

fun Double.format(): String =
    String.format("%.3f", this)

fun List<Double>.product(): Double =
    foldRight(1.0) { x, y -> x * y }

fun List<Double>.geometricMean(): Double =
    product().pow(1.0 / size)

fun List<Double>.geometricStandardDeviation(): Double =
    exp(this.map { ln(it) }.standardDeviation())
