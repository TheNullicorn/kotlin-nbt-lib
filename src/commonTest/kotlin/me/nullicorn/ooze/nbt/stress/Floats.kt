package me.nullicorn.ooze.nbt.stress

internal val floatValues: Array<Float>
    get() = floatArrayOf(
        Float.NEGATIVE_INFINITY,
        Float.MIN_VALUE,
        Float.MIN_VALUE / 2,

        Long.MIN_VALUE.toFloat(),
        Long.MIN_VALUE.toFloat() / 2,

        Int.MIN_VALUE.toFloat(),
        Int.MIN_VALUE.toFloat() / 2,

        Short.MIN_VALUE.toFloat(),
        Short.MIN_VALUE.toFloat() / 2,

        Byte.MIN_VALUE.toFloat(),
        Byte.MIN_VALUE.toFloat() / 2,

        -kotlin.math.PI.toFloat(),
        -kotlin.math.E.toFloat(),

        -1.0f / 3, // -1/3
        -2.0f,
        -1.0f,
        0.0f,
        1.0f,
        2.0f,
        1.0f / 3, // -1/3

        kotlin.math.PI.toFloat(),
        kotlin.math.E.toFloat(),

        Byte.MAX_VALUE.toFloat() / 2,
        Byte.MAX_VALUE.toFloat(),

        Short.MAX_VALUE.toFloat() / 2,
        Short.MAX_VALUE.toFloat(),

        Int.MAX_VALUE.toFloat() / 2,
        Int.MAX_VALUE.toFloat(),

        Long.MAX_VALUE.toFloat() / 2,
        Long.MAX_VALUE.toFloat(),

        Float.MAX_VALUE / 2,
        Float.MAX_VALUE,
        Float.POSITIVE_INFINITY
    ).toTypedArray()