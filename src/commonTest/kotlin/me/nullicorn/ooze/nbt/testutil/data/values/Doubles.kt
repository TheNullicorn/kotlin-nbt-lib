package me.nullicorn.ooze.nbt.testutil.data.values

internal val doubleValues: Array<Double>
    get() = doubleArrayOf(
        Double.NEGATIVE_INFINITY,
        Double.MIN_VALUE,
        Double.MIN_VALUE / 2,

        Float.NEGATIVE_INFINITY.toDouble(),
        Float.MIN_VALUE.toDouble(),
        Float.MIN_VALUE.toDouble() / 2,

        Long.MIN_VALUE.toDouble(),
        Long.MIN_VALUE.toDouble() / 2,

        Int.MIN_VALUE.toDouble(),
        Int.MIN_VALUE.toDouble() / 2,

        Short.MIN_VALUE.toDouble(),
        Short.MIN_VALUE.toDouble() / 2,

        Byte.MIN_VALUE.toDouble(),
        Byte.MIN_VALUE.toDouble() / 2,

        -kotlin.math.PI,
        -kotlin.math.E,

        -1.0 / 3, // -1/3
        -2.0,
        -1.0,
        0.0,
        1.0,
        2.0,
        1.0 / 3, // -1/3

        kotlin.math.PI,
        kotlin.math.E,

        Byte.MAX_VALUE.toDouble() / 2,
        Byte.MAX_VALUE.toDouble(),

        Short.MAX_VALUE.toDouble() / 2,
        Short.MAX_VALUE.toDouble(),

        Int.MAX_VALUE.toDouble() / 2,
        Int.MAX_VALUE.toDouble(),

        Long.MAX_VALUE.toDouble() / 2,
        Long.MAX_VALUE.toDouble(),

        Float.MAX_VALUE.toDouble() / 2,
        Float.MAX_VALUE.toDouble(),
        Float.POSITIVE_INFINITY.toDouble(),

        Double.MAX_VALUE / 2,
        Double.MAX_VALUE,
        Double.POSITIVE_INFINITY
    ).toTypedArray()