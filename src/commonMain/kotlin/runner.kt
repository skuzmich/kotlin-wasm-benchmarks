import org.jetbrains.ring.*

fun reportBenchmark(name: String, time: Double) {
    log("Bench : $platform : $name : $time")
}

fun runBenchmark(
    name: String,
    benchmark: () -> Any?,
    numWarmIterations: Int = recommendedWarmup,
    numberOfAttempts: Int = 10
) {
    repeat(numWarmIterations) {
        benchmark()
    }

    repeat(numberOfAttempts) {
        val start = performanceNow()
        benchmark()
        val time = performanceNow() - start
        reportBenchmark(name, time)
    }
}


fun main() {
    val start = performanceNow()

    val castsBenchmark = CastsBenchmark()
    runBenchmark("Casts.classCast", { castsBenchmark.classCast() })
    runBenchmark("Casts.interfaceCast", { castsBenchmark.interfaceCast() })

    val allocationBenchmark = AllocationBenchmark()
    runBenchmark("AllocationBenchmark.allocateObjects", { allocationBenchmark.allocateObjects() })

    val callsBenchmark = CallsBenchmark()
    runBenchmark("Calls.finalMethod", { callsBenchmark.finalMethodCall() })
    runBenchmark("Calls.interfaceMethodBimorphic", { callsBenchmark.interfaceMethodCall_BimorphicCallsite() })
    runBenchmark("Calls.interfaceMethodHexamorphic", { callsBenchmark.interfaceMethodCall_HexamorphicCallsite() })
    runBenchmark("Calls.interfaceMethodMonomorphic", { callsBenchmark.interfaceMethodCall_MonomorphicCallsite() })
    runBenchmark("Calls.interfaceMethodTrimorphic", { callsBenchmark.interfaceMethodCall_TrimorphicCallsite() })
    runBenchmark("Calls.openMethodBimorphic", { callsBenchmark.classOpenMethodCall_BimorphicCallsite() })
    runBenchmark("Calls.openMethodMonomorphic", { callsBenchmark.classOpenMethodCall_MonomorphicCallsite() })
    runBenchmark("Calls.openMethodTrimorphic", { callsBenchmark.classOpenMethodCall_TrimorphicCallsite() })
    runBenchmark("Calls.parameterBoxUnboxFolding", { callsBenchmark.parameterBoxUnboxFolding() })
    runBenchmark("Calls.returnBoxUnboxFolding", { callsBenchmark.returnBoxUnboxFolding() })

    val companionObjectBenchmark = CompanionObjectBenchmark()
    runBenchmark("CompanionObject.invokeRegularFunction", { companionObjectBenchmark.invokeRegularFunction() })

    val elvisBenchmark = ElvisBenchmark()
    runBenchmark("Elvis.testCompositeElvis", { elvisBenchmark.testCompositeElvis() })

    val fibonacciBenchmark = FibonacciBenchmark()
    runBenchmark("Fibonacci.calc", { fibonacciBenchmark.calc() })
    runBenchmark("Fibonacci.calcClassic", { fibonacciBenchmark.calcClassic() })
    runBenchmark("Fibonacci.calcSquare", { fibonacciBenchmark.calcSquare() })
    runBenchmark("Fibonacci.calcWithProgression", { fibonacciBenchmark.calcWithProgression() })

    val forLoopsBenchmark = ForLoopsBenchmark()
    runBenchmark("ForLoops.arrayIndicesLoop", { forLoopsBenchmark.arrayIndicesLoop() })
    runBenchmark("ForLoops.arrayLoop", { forLoopsBenchmark.arrayLoop() })
    runBenchmark("ForLoops.charArrayIndicesLoop", { forLoopsBenchmark.charArrayIndicesLoop() })
    runBenchmark("ForLoops.charArrayLoop", { forLoopsBenchmark.charArrayLoop() })
    runBenchmark("ForLoops.floatArrayIndicesLoop", { forLoopsBenchmark.floatArrayIndicesLoop() })
    runBenchmark("ForLoops.floatArrayLoop", { forLoopsBenchmark.floatArrayLoop() })
    runBenchmark("ForLoops.intArrayIndicesLoop", { forLoopsBenchmark.intArrayIndicesLoop() })
    runBenchmark("ForLoops.intArrayLoop", { forLoopsBenchmark.intArrayLoop() })

    val inheritanceBenchmark = InheritanceBenchmark()
    runBenchmark("Inheritance.baseCalls", { inheritanceBenchmark.baseCalls() })

    val inlineBenchmark = InlineBenchmark()
    runBenchmark("Inline.calculate", { inlineBenchmark.calculate() })
    runBenchmark("Inline.calculateGeneric", { inlineBenchmark.calculateGeneric() })
    runBenchmark("Inline.calculateGenericInline", { inlineBenchmark.calculateGenericInline() })
    runBenchmark("Inline.calculateInline", { inlineBenchmark.calculateInline() })

    val lambdaBenchmark = LambdaBenchmark()
    runBenchmark("Lambda.capturingLambda", { lambdaBenchmark.capturingLambda() })
    runBenchmark("Lambda.capturingLambdaNoInline", { lambdaBenchmark.capturingLambdaNoInline() })
    runBenchmark("Lambda.methodReference", { lambdaBenchmark.methodReference() })
    runBenchmark("Lambda.methodReferenceNoInline", { lambdaBenchmark.methodReferenceNoInline() })
    runBenchmark("Lambda.mutatingLambda", { lambdaBenchmark.mutatingLambda() })
    runBenchmark("Lambda.mutatingLambdaNoInline", { lambdaBenchmark.mutatingLambdaNoInline() })
    runBenchmark("Lambda.noncapturingLambda", { lambdaBenchmark.noncapturingLambda() })
    runBenchmark("Lambda.noncapturingLambdaNoInline", { lambdaBenchmark.noncapturingLambdaNoInline() })

    val singletonBenchmark = SingletonBenchmark()
    runBenchmark("Singleton.access", { singletonBenchmark.access() })

    val switchBenchmark = SwitchBenchmark()
    runBenchmark("Switch.testConstSwitch", { switchBenchmark.testConstSwitch() })
    runBenchmark("Switch.testDenseEnumsSwitch", { switchBenchmark.testDenseEnumsSwitch() })
    runBenchmark("Switch.testDenseIntSwitch", { switchBenchmark.testDenseIntSwitch() })
    runBenchmark("Switch.testEnumsSwitch", { switchBenchmark.testEnumsSwitch() })
    runBenchmark("Switch.testObjConstSwitch", { switchBenchmark.testObjConstSwitch() })
    runBenchmark("Switch.testSealedWhenSwitch", { switchBenchmark.testSealedWhenSwitch() })
    runBenchmark("Switch.testSparseIntSwitch", { switchBenchmark.testSparseIntSwitch() })
    runBenchmark("Switch.testStringsSwitch", { switchBenchmark.testStringsSwitch() })
    runBenchmark("Switch.testVarSwitch", { switchBenchmark.testVarSwitch() })

    log("TOTAL TIME: ${performanceNow() - start}")
}


//    val defaultArgumentBenchmark = DefaultArgumentBenchmark()
//    runBenchmark("DefaultArgument.testEightOfEight", { defaultArgumentBenchmark.testEightOfEight() })
//    runBenchmark("DefaultArgument.testFourOfFour", { defaultArgumentBenchmark.testFourOfFour() })
//    runBenchmark("DefaultArgument.testOneOfEight", { defaultArgumentBenchmark.testOneOfEight() })
//    runBenchmark("DefaultArgument.testOneOfFour", { defaultArgumentBenchmark.testOneOfFour() })
//    runBenchmark("DefaultArgument.testOneOfTwo", { defaultArgumentBenchmark.testOneOfTwo() })
//    runBenchmark("DefaultArgument.testTwoOfTwo", { defaultArgumentBenchmark.testTwoOfTwo() })


//    val parameterNNA = ParameterNotNullAssertionBenchmark()
//    runBenchmark("ParameterNotNull.invokeEightArgsWithNullCheck", { parameterNNA.invokeEightArgsWithNullCheck() })
//    runBenchmark("ParameterNotNull.invokeEightArgsWithoutNullCheck", { parameterNNA.invokeEightArgsWithoutNullCheck() })
//    runBenchmark("ParameterNotNull.invokeOneArgWithNullCheck", { parameterNNA.invokeOneArgWithNullCheck() })
//    runBenchmark("ParameterNotNull.invokeOneArgWithoutNullCheck", { parameterNNA.invokeOneArgWithoutNullCheck() })
//    runBenchmark("ParameterNotNull.invokeTwoArgsWithNullCheck", { parameterNNA.invokeTwoArgsWithNullCheck() })
//    runBenchmark("ParameterNotNull.invokeTwoArgsWithoutNullCheck", { parameterNNA.invokeTwoArgsWithoutNullCheck() })



//runBenchmark("Casts.classCast", { castsBenchmark.classCast() })
//runBenchmark("Casts.interfaceCast", { castsBenchmark.interfaceCast() })
//runBenchmark("ClassArray.copy", { ClassArrayBenchmark().copy() })
//runBenchmark("ClassArray.copyManual", { ClassArrayBenchmark().copyManual() })
//runBenchmark("ClassArray.countFiltered", { ClassArrayBenchmark().countFiltered() })
//runBenchmark("ClassArray.countFilteredLocal", { ClassArrayBenchmark().countFilteredLocal() })
//runBenchmark("ClassArray.countFilteredManual", { ClassArrayBenchmark().countFilteredManual() })
//runBenchmark("ClassArray.filter", { ClassArrayBenchmark().filter() })
//runBenchmark("ClassArray.filterAndCount", { ClassArrayBenchmark().filterAndCount() })
//runBenchmark("ClassArray.filterAndMap", { ClassArrayBenchmark().filterAndMap() })
//runBenchmark("ClassArray.filterAndMapManual", { ClassArrayBenchmark().filterAndMapManual() })
//runBenchmark("ClassArray.filterManual", { ClassArrayBenchmark().filterManual() })
//runBenchmark("ClassBaseline.allocateArray", { ClassBaselineBenchmark().allocateArray() })
//runBenchmark("ClassBaseline.allocateArrayAndFill", { ClassBaselineBenchmark().allocateArrayAndFill() })
//runBenchmark("ClassBaseline.allocateList", { ClassBaselineBenchmark().allocateList() })
//runBenchmark("ClassBaseline.allocateListAndFill", { ClassBaselineBenchmark().allocateListAndFill() })
//runBenchmark("ClassBaseline.allocateListAndWrite", { ClassBaselineBenchmark().allocateListAndWrite() })
//runBenchmark("ClassBaseline.consume", { ClassBaselineBenchmark().consume() })
//runBenchmark("ClassBaseline.consumeField", { ClassBaselineBenchmark().consumeField() })
//runBenchmark("ClassList.copy", { ClassListBenchmark().copy() })
//runBenchmark("ClassList.copyManual", { ClassListBenchmark().copyManual() })
//runBenchmark("ClassList.countFiltered", { ClassListBenchmark().countFiltered() })
//runBenchmark("ClassList.countFilteredManual", { ClassListBenchmark().countFilteredManual() })
//runBenchmark("ClassList.countWithLambda", { ClassListBenchmark().countWithLambda() })
//runBenchmark("ClassList.filter", { ClassListBenchmark().filter() })
//runBenchmark("ClassList.filterAndCount", { ClassListBenchmark().filterAndCount() })
//runBenchmark("ClassList.filterAndCountWithLambda", { ClassListBenchmark().filterAndCountWithLambda() })
//runBenchmark("ClassList.filterAndMap", { ClassListBenchmark().filterAndMap() })
//runBenchmark("ClassList.filterAndMapManual", { ClassListBenchmark().filterAndMapManual() })
//runBenchmark("ClassList.filterAndMapWithLambda", { ClassListBenchmark().filterAndMapWithLambda() })
//runBenchmark("ClassList.filterAndMapWithLambdaAsSequence", { ClassListBenchmark().filterAndMapWithLambdaAsSequence() })
//runBenchmark("ClassList.filterManual", { ClassListBenchmark().filterManual() })
//runBenchmark("ClassList.filterWithLambda", { ClassListBenchmark().filterWithLambda() })
//runBenchmark("ClassList.mapWithLambda", { ClassListBenchmark().mapWithLambda() })
//runBenchmark("ClassList.reduce", { ClassListBenchmark().reduce() })
//runBenchmark("ClassStream.copy", { ClassStreamBenchmark().copy() })
//runBenchmark("ClassStream.copyManual", { ClassStreamBenchmark().copyManual() })
//runBenchmark("ClassStream.countFiltered", { ClassStreamBenchmark().countFiltered() })
//runBenchmark("ClassStream.countFilteredManual", { ClassStreamBenchmark().countFilteredManual() })
//runBenchmark("ClassStream.filter", { ClassStreamBenchmark().filter() })
//runBenchmark("ClassStream.filterAndCount", { ClassStreamBenchmark().filterAndCount() })
//runBenchmark("ClassStream.filterAndMap", { ClassStreamBenchmark().filterAndMap() })
//runBenchmark("ClassStream.filterAndMapManual", { ClassStreamBenchmark().filterAndMapManual() })
//runBenchmark("ClassStream.filterManual", { ClassStreamBenchmark().filterManual() })
//runBenchmark("ClassStream.reduce", { ClassStreamBenchmark().reduce() })
//runBenchmark("CoordinatesSolver.solve", { CoordinatesSolverBenchmark().solve() })
//runBenchmark("Elvis.testElvis", { ElvisBenchmark().testElvis() })

//runBenchmark("Euler.problem1", { EulerBenchmark().problem1() })
//runBenchmark("Euler.problem14", { EulerBenchmark().problem14() })
//runBenchmark("Euler.problem14full", { EulerBenchmark().problem14full() })
//runBenchmark("Euler.problem1bySequence", { EulerBenchmark().problem1bySequence() })
//runBenchmark("Euler.problem2", { EulerBenchmark().problem2() })
//runBenchmark("Euler.problem4", { EulerBenchmark().problem4() })
//runBenchmark("Euler.problem8", { EulerBenchmark().problem8() })
//runBenchmark("Euler.problem9", { EulerBenchmark().problem9() })

//runBenchmark("ForLoops.stringIndicesLoop", { ForLoopsBenchmark().stringIndicesLoop() })
//runBenchmark("ForLoops.stringLoop", { ForLoopsBenchmark().stringLoop() })
//runBenchmark("GraphSolver.solve", { GraphSolverBenchmark().solve() })


//runBenchmark("IntArray.copy", { IntArrayBenchmark().copy() })
//runBenchmark("IntArray.copyManual", { IntArrayBenchmark().copyManual() })
//runBenchmark("IntArray.countFiltered", { IntArrayBenchmark().countFiltered() })
//runBenchmark("IntArray.countFilteredLocal", { IntArrayBenchmark().countFilteredLocal() })
//runBenchmark("IntArray.countFilteredManual", { IntArrayBenchmark().countFilteredManual() })
//runBenchmark("IntArray.countFilteredPrime", { IntArrayBenchmark().countFilteredPrime() })
//runBenchmark("IntArray.countFilteredPrimeManual", { IntArrayBenchmark().countFilteredPrimeManual() })
//runBenchmark("IntArray.countFilteredSome", { IntArrayBenchmark().countFilteredSome() })
//runBenchmark("IntArray.countFilteredSomeLocal", { IntArrayBenchmark().countFilteredSomeLocal() })
//runBenchmark("IntArray.countFilteredSomeManual", { IntArrayBenchmark().countFilteredSomeManual() })
//runBenchmark("IntArray.filter", { IntArrayBenchmark().filter() })
//runBenchmark("IntArray.filterAndCount", { IntArrayBenchmark().filterAndCount() })
//runBenchmark("IntArray.filterAndMap", { IntArrayBenchmark().filterAndMap() })
//runBenchmark("IntArray.filterAndMapManual", { IntArrayBenchmark().filterAndMapManual() })
//runBenchmark("IntArray.filterManual", { IntArrayBenchmark().filterManual() })
//runBenchmark("IntArray.filterPrime", { IntArrayBenchmark().filterPrime() })
//runBenchmark("IntArray.filterSome", { IntArrayBenchmark().filterSome() })
//runBenchmark("IntArray.filterSomeAndCount", { IntArrayBenchmark().filterSomeAndCount() })
//runBenchmark("IntArray.filterSomeManual", { IntArrayBenchmark().filterSomeManual() })
//runBenchmark("IntArray.reduce", { IntArrayBenchmark().reduce() })
//runBenchmark("IntBaseline.allocateArray", { IntBaselineBenchmark().allocateArray() })
//runBenchmark("IntBaseline.allocateArrayAndFill", { IntBaselineBenchmark().allocateArrayAndFill() })
//runBenchmark("IntBaseline.allocateList", { IntBaselineBenchmark().allocateList() })
//runBenchmark("IntBaseline.allocateListAndFill", { IntBaselineBenchmark().allocateListAndFill() })
//runBenchmark("IntBaseline.consume", { IntBaselineBenchmark().consume() })
//runBenchmark("IntList.copy", { IntListBenchmark().copy() })
//runBenchmark("IntList.copyManual", { IntListBenchmark().copyManual() })
//runBenchmark("IntList.countFiltered", { IntListBenchmark().countFiltered() })
//runBenchmark("IntList.countFilteredLocal", { IntListBenchmark().countFilteredLocal() })
//runBenchmark("IntList.countFilteredManual", { IntListBenchmark().countFilteredManual() })
//runBenchmark("IntList.filter", { IntListBenchmark().filter() })
//runBenchmark("IntList.filterAndCount", { IntListBenchmark().filterAndCount() })
//runBenchmark("IntList.filterAndMap", { IntListBenchmark().filterAndMap() })
//runBenchmark("IntList.filterAndMapManual", { IntListBenchmark().filterAndMapManual() })
//runBenchmark("IntList.filterManual", { IntListBenchmark().filterManual() })
//runBenchmark("IntList.reduce", { IntListBenchmark().reduce() })
//runBenchmark("IntStream.copy", { IntStreamBenchmark().copy() })
//runBenchmark("IntStream.copyManual", { IntStreamBenchmark().copyManual() })
//runBenchmark("IntStream.countFiltered", { IntStreamBenchmark().countFiltered() })
//runBenchmark("IntStream.countFilteredLocal", { IntStreamBenchmark().countFilteredLocal() })
//runBenchmark("IntStream.countFilteredManual", { IntStreamBenchmark().countFilteredManual() })
//runBenchmark("IntStream.filter", { IntStreamBenchmark().filter() })
//runBenchmark("IntStream.filterAndCount", { IntStreamBenchmark().filterAndCount() })
//runBenchmark("IntStream.filterAndMap", { IntStreamBenchmark().filterAndMap() })
//runBenchmark("IntStream.filterAndMapManual", { IntStreamBenchmark().filterAndMapManual() })
//runBenchmark("IntStream.filterManual", { IntStreamBenchmark().filterManual() })
//runBenchmark("IntStream.reduce", { IntStreamBenchmark().reduce() })

//runBenchmark("LinkedListWithAtomicsBenchmark", { LinkedListWithAtomicsBenchmark().ensureNext() })
//runBenchmark("LocalObjects.localArray", { LocalObjectsBenchmark().localArray() })
//runBenchmark("Loop.arrayForeachLoop", { LoopBenchmark().arrayForeachLoop() })
//runBenchmark("Loop.arrayIndexLoop", { LoopBenchmark().arrayIndexLoop() })
//runBenchmark("Loop.arrayListForeachLoop", { LoopBenchmark().arrayListForeachLoop() })
//runBenchmark("Loop.arrayListLoop", { LoopBenchmark().arrayListLoop() })
//runBenchmark("Loop.arrayLoop", { LoopBenchmark().arrayLoop() })
//runBenchmark("Loop.arrayWhileLoop", { LoopBenchmark().arrayWhileLoop() })
//runBenchmark("Loop.rangeLoop", { LoopBenchmark().rangeLoop() })
//runBenchmark("MatrixMap.add", { MatrixMapBenchmark().add() })

    // TODO:
//runBenchmark("OctoTest", ::octoTest)


//runBenchmark("PrimeList.calcDirect", { PrimeListBenchmark().calcDirect() })
//runBenchmark("PrimeList.calcEratosthenes", { PrimeListBenchmark().calcEratosthenes() })

//runBenchmark("String.stringBuilderConcat", { StringBenchmark().stringBuilderConcat() })
//runBenchmark("String.stringBuilderConcatNullable", { StringBenchmark().stringBuilderConcatNullable() })
//runBenchmark("String.stringConcat", { StringBenchmark().stringConcat() })
//runBenchmark("String.stringConcatNullable", { StringBenchmark().stringConcatNullable() })
//runBenchmark("String.summarizeSplittedCsv", { StringBenchmark().summarizeSplittedCsv() })

//runBenchmark("WithIndicies.withIndicies", { WithIndiciesBenchmark().withIndicies() })
//runBenchmark("WithIndicies.withIndiciesManual", { WithIndiciesBenchmark().withIndiciesManual() })



//    runBenchmark("final", {calls.finalMethodCall()})
