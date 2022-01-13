package me.nullicorn.ooze.nbt.test.data

internal val byteArrayValues: Array<ByteArray>
    get() = arrayOf(
        // Every byte value.
        byteValues.toByteArray(),

        // Only zeros.
        ByteArray(52985),

        // The array seen in the official file, "bigtest.nbt".
        ByteArray(1000) { n -> ((n * n * 255 + n * 7) % 100).toByte() },
    )