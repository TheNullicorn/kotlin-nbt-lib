package me.nullicorn.ooze.nbt.io.codec

import me.nullicorn.ooze.nbt.io.InputException
import me.nullicorn.ooze.nbt.io.runUnsafeInput
import me.nullicorn.ooze.nbt.io.source.Source

private const val SIZE_BYTES = 2

/**
 * Consumes the next `2` bytes from the source, and returns their combined `16`-bit value.
 *
 * @param[endian] The order to combine the bytes in.
 * @return the combined value of the bytes, as an integer, ordered by the specified
 * [endianness][endian].
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * consume, or if it becomes exhausted during the operation.
 */
internal fun Source.readShort(endian: Endianness = Endianness.BIG) =
    runUnsafeInput("reading short") {
        readArbitrarySizeInt(length = SIZE_BYTES, endian).toShort()
    }

/**
 * Discards the next `2` bytes from the source.
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * discard, or if it becomes exhausted during the operation.
 */
internal fun Source.skipShort() = runUnsafeInput("skipping short") {
    skip(length = SIZE_BYTES)
}