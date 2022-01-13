package me.nullicorn.ooze.nbt.test.data

internal val intArrayValues: Array<IntArray>
    get() = arrayOf(
        // Every byte value.
        intValues.toIntArray(),

        // Only zeros.
        IntArray(52985),

        // Various ints.
        complexIntArray,

        // Negative version of complexIntArray.
        complexIntArray.map { value -> -value }.toIntArray()
    )

/**
 * A modified version of the ByteArray seen in the official file, "bigtest.nbt".
 */
private val complexIntArray: IntArray
    get() = IntArray(1000) { n -> (n * n * 255 + n * 7) % (Int.MIN_VALUE / 50) }