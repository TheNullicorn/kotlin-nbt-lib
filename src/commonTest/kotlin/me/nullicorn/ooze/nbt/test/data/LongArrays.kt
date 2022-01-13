package me.nullicorn.ooze.nbt.test.data

internal val longArrayValues: Array<LongArray>
    get() = arrayOf(
        // Every byte value.
        longValues.toLongArray(),

        // Only zeros.
        LongArray(52985),

        // Various ints.
        complexLongArray,

        // Negative version of complexIntArray.
        complexLongArray.map { value -> -value }.toLongArray()
    )

/**
 * A modified version of the ByteArray seen in the official file, "bigtest.nbt".
 */
private val complexLongArray: LongArray
    get() = LongArray(1000) { n ->
        (n.toLong() * n * Short.MAX_VALUE + n * 7) % (UInt.MAX_VALUE.toLong() / 50)
    }