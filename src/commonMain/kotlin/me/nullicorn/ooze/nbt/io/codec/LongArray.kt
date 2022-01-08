package me.nullicorn.ooze.nbt.io.codec

import me.nullicorn.ooze.nbt.io.InputException
import me.nullicorn.ooze.nbt.io.runUnsafeInput
import me.nullicorn.ooze.nbt.io.source.Source

private const val EACH_SIZE_BYTES = 8

/**
 * Consumes `8` bytes for each `64`-bit int in an array with the supplied [length].
 *
 * @param[length] The number of `8`-byte ints to read from the stream.
 * @param[endian] The order to combine each int's bytes in. This does not affect the order in which
 * the ints themselves are read.
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * consume, or if it becomes exhausted during the operation.
 */
internal fun Source.readLongArray(
    length: Int,
    endian: Endianness = Endianness.BIG,
): LongArray {
    require(length >= 0) { "Array length cannot be negative ($length)" }

    val bytes = runUnsafeInput("reading long array") {
        readToNewBuffer(length * EACH_SIZE_BYTES)
    }

    return LongArray(length) { index ->
        bytes.readArbitrarySizeLong(EACH_SIZE_BYTES, endian, offset = index * EACH_SIZE_BYTES)
    }
}

/**
 * Discards `4` bytes in the stream for each int in an array with the supplied [length].
 *
 * @param[length] The number of `4`-byte ints to skip.
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * discard, or if it becomes exhausted during the operation.
 */
internal fun Source.skipLongArray(length: Int) {
    require(length >= 0) { "Array length cannot be negative ($length)" }

    runUnsafeInput("skipping long array") {
        skip(length * EACH_SIZE_BYTES)
    }
}