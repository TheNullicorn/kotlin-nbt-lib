package me.nullicorn.ooze.nbt.io.codec

import me.nullicorn.ooze.nbt.io.InputException
import me.nullicorn.ooze.nbt.io.runUnsafeInput
import me.nullicorn.ooze.nbt.io.source.Source

private const val SIZE_BYTES = 8

/**
 * Used by [readArbitrarySizeLong] so that the entire long can be read at once without allocating a
 * new array every time.
 */
private val arbitraryLongBuffer by lazy { ByteArray(SIZE_BYTES) }

/**
 * Consumes the next `8` bytes from the source, and returns their combined `64`-bit value.
 *
 * @param[endian] The order to combine the bytes in.
 * @return the combined value of the bytes, as an integer, ordered by the specified
 * [endianness][endian].
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * consume, or if it becomes exhausted during the operation.
 */
internal fun Source.readLong(endian: Endianness = Endianness.BIG) =
    runUnsafeInput("reading long") {
        readArbitrarySizeLong(length = SIZE_BYTES, endian)
    }

/**
 * Discards the next `8` bytes from the source.
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * discard, or if it becomes exhausted during the operation.
 */
internal fun Source.skipLong() = runUnsafeInput("skipping long") {
    skip(SIZE_BYTES)
}

/**
 * Reads the next `n` bytes from the source, interpreting them as a signed, two's complement
 * integer.
 *
 * This function only supports integers up to `8` bytes (`64` bits).
 *
 * @param[length] The value of `n`; the number of bytes to read from the source.
 * @param[endian] The order in which to combine the bytes into an [Long].
 * @return the bytes, combined as a `64`-bit signed integer.
 *
 * @throws[IllegalArgumentException] if [length] is less than `0`.
 * @throws[IllegalArgumentException] if [length] is greater than `8`.
 */
internal fun Source.readArbitrarySizeLong(length: Int, endian: Endianness): Long {
    checkArbitraryLength(length)

    val bytes = readToBuffer(arbitraryLongBuffer, offset = 0, length)
    return bytes.readArbitrarySizeLong(length, endian)
}

/**
 * Combines `n` bytes from the array into a [Long], starting at `i = `[offset].
 *
 * @param[length] `n`, the number of bytes to combine.
 * @param[endian] The order to combine the bytes in.
 * @param[offset] The index of the first byte to combine.
 * @return the value of the combined bytes, as a [Long].
 *
 * @throws[IllegalArgumentException] if [length] is less than `0`.
 * @throws[IllegalArgumentException] if [length] is greater than `8`.
 * @throws[IndexOutOfBoundsException] if [offset] is less than `0`.
 * @throws[IndexOutOfBoundsException] if [offset] + [length] is greater than the array's
 * [lastIndex].
 */
internal fun ByteArray.readArbitrarySizeLong(
    length: Int,
    endian: Endianness,
    offset: Int = 0,
): Long {
    checkArbitraryLength(length)
    if (offset < 0) {
        throw IndexOutOfBoundsException("offset < 0 (size=$size)")
    } else if (offset + length > lastIndex) {
        throw IndexOutOfBoundsException("$offset is too high (size=$size)")
    }

    var result = 0L

    for (i in 0 until length) {
        val bytesToShift =
            if (endian == Endianness.LITTLE) i
            else length - i - 1

        val bits = this[offset + i].toLong()
        result = result or (bits shl (Byte.SIZE_BITS * bytesToShift))
    }

    return result
}

/**
 * @throws[IllegalArgumentException] if the [length] is negative, or if it is too many bytes to hold
 * in a single [Long].
 */
private fun checkArbitraryLength(length: Int) {
    require(length >= 0) { "Long cannot use a negative number of bytes: $length" }
    require(length <= SIZE_BYTES) { "Long can only hold up to $SIZE_BYTES bytes, not $length" }
}