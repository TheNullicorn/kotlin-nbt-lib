package me.nullicorn.ooze.nbt.io.codec

import me.nullicorn.ooze.nbt.io.source.InputSource
import me.nullicorn.ooze.nbt.io.InputException
import me.nullicorn.ooze.nbt.io.runUnsafeInput

private const val SIZE_BYTES = 8

/**
 * Consumes the next `8` bytes from the source, and returns their combined `64`-bit value,
 * decoded according to `IEEE 754`.
 *
 * @param[endian] The order to combine the bytes in.
 * @return the combined value of the bytes, as a floating-point number, ordered by the specified
 * [endianness][endian].
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * consume, or if it becomes exhausted during the operation.
 */
internal fun InputSource.readDouble(endian: Endianness = Endianness.BIG) =
    runUnsafeInput("reading double") {
        Double.fromBits(readArbitrarySizeLong(length = SIZE_BYTES, endian))
    }

/**
 * Discards the next `8` bytes from the source.
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * discard, or if it becomes exhausted during the operation.
 */
internal fun InputSource.skipDouble() = runUnsafeInput("skipping double") {
    skip(SIZE_BYTES)
}