package me.nullicorn.ooze.nbt.testutil.data.values

internal val shortValues: Array<Short>
    get() = shortArrayOf(
        Short.MIN_VALUE,
        (Short.MIN_VALUE / 2).toShort(),
        Byte.MIN_VALUE.toShort(),
        -42,
        -10,
        -2,
        -1,
        0,
        1,
        2,
        10,
        42,
        Byte.MAX_VALUE.toShort(),
        (Short.MAX_VALUE / 2).toShort(),
        Short.MAX_VALUE,
    ).toTypedArray()