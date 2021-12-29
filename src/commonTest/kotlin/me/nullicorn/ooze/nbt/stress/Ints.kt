package me.nullicorn.ooze.nbt.stress

internal val intValues: Array<Int>
    get() = intArrayOf(
        Int.MIN_VALUE,
        Int.MIN_VALUE / 2,

        Short.MIN_VALUE.toInt(),
        Short.MIN_VALUE / 2,

        Byte.MIN_VALUE.toInt(),
        Byte.MIN_VALUE / 2,

        -2,
        -1,
        0,
        1,
        2,

        Byte.MAX_VALUE / 2,
        Byte.MAX_VALUE.toInt(),

        Short.MAX_VALUE / 2,
        Short.MAX_VALUE.toInt(),

        Int.MAX_VALUE / 2,
        Int.MAX_VALUE
    ).toTypedArray()