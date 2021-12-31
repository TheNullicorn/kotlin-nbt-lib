package me.nullicorn.ooze.nbt.data.values

internal val longValues: Array<Long>
    get() = longArrayOf(
        Long.MIN_VALUE,
        Long.MIN_VALUE / 2,

        Int.MIN_VALUE.toLong(),
        Int.MIN_VALUE.toLong() / 2,

        Short.MIN_VALUE.toLong(),
        Short.MIN_VALUE.toLong() / 2,

        Byte.MIN_VALUE.toLong(),
        Byte.MIN_VALUE.toLong() / 2,

        -2,
        -1,
        0,
        1,
        2,

        Byte.MAX_VALUE.toLong() / 2,
        Byte.MAX_VALUE.toLong(),

        Short.MAX_VALUE.toLong() / 2,
        Short.MAX_VALUE.toLong(),

        Int.MAX_VALUE.toLong() / 2,
        Int.MAX_VALUE.toLong(),

        Long.MAX_VALUE / 2,
        Long.MAX_VALUE
    ).toTypedArray()