package me.nullicorn.ooze.nbt.io.codec

import me.nullicorn.ooze.nbt.io.InputException
import me.nullicorn.ooze.nbt.io.runUnsafeInput
import me.nullicorn.ooze.nbt.io.source.InputSource

private const val SIZE_BYTES = 4

/**
 * Consumes the next `4` bytes from the source, and returns their combined `32`-bit value,
 * decoded according to `IEEE 754`.
 *
 * @param[endian] The order to combine the bytes in.
 * @return the combined value of the bytes, as a floating-point number, ordered by the specified
 * [endianness][endian].
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * consume, or if it becomes exhausted during the operation.
 */
internal fun InputSource.readFloat(endian: Endianness = Endianness.BIG) =
    runUnsafeInput("reading float") {
        Float.fromBits(readArbitrarySizeInt(length = SIZE_BYTES, endian))
    }

/**
 * Discards the next `4` bytes from the source.
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * discard, or if it becomes exhausted during the operation.
 */
internal fun InputSource.skipFloat() = runUnsafeInput("skipping float") {
    skip(SIZE_BYTES)
}
