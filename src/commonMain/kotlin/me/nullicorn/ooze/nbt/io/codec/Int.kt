package me.nullicorn.ooze.nbt.io.codec

import me.nullicorn.ooze.nbt.io.InputException
import me.nullicorn.ooze.nbt.io.runUnsafeInput
import me.nullicorn.ooze.nbt.io.source.InputSource

private const val SIZE_BYTES = 4

/**
 * Used by [readArbitrarySizeInt] so that the entire int can be read at once without allocating a
 * new array every time.
 */
private val arbitraryIntBuffer by lazy { ByteArray(SIZE_BYTES) }

/**
 * Consumes the next `4` bytes from the source, and returns their combined `32`-bit value.
 *
 * @param[endian] The order to combine the bytes in.
 * @return the combined value of the bytes, as an integer, ordered by the specified
 * [endianness][endian].
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * consume, or if it becomes exhausted during the operation.
 */
internal fun InputSource.readInt(endian: Endianness = Endianness.BIG) =
    runUnsafeInput("reading int") {
        readArbitrarySizeInt(length = SIZE_BYTES, endian)
    }

/**
 * Discards the next `4` bytes from the source.
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * discard, or if it becomes exhausted during the operation.
 */
internal fun InputSource.skipInt() = runUnsafeInput("skipping int") {
    skip(length = SIZE_BYTES)
}

/**
 * Reads the next `n` bytes from the source, interpreting them as a signed, two's complement
 * integer.
 *
 * This function only supports untegers up to `4` bytes (`32` bits). For larger values, use
 * [readArbitrarySizeLong].
 *
 * @param[length] The value of `n`; the number of bytes to read from the source.
 * @param[endian] The order in which to combine the bytes into an [Int].
 * @return the bytes, combined as a `32`-bit signed integer.
 * @see[readArbitrarySizeLong]
 *
 * @throws[IllegalArgumentException] if [length] is less than `0`.
 * @throws[IllegalArgumentException] if [length] is greater than `8`.
 */
internal fun InputSource.readArbitrarySizeInt(length: Int, endian: Endianness): Int {
    checkArbitraryLength(length)

    val bytes = readToBuffer(arbitraryIntBuffer, offset = 0, length)
    return bytes.readArbitrarySizeInt(length, endian)
}

/**
 * Combines `n` bytes from the array into a [Int], starting at `i = `[offset].
 *
 * @param[length] `n`, the number of bytes to combine.
 * @param[endian] The order to combine the bytes in.
 * @param[offset] The index of the first byte to combine.
 * @return the value of the combined bytes, as a [Int].
 *
 * @throws[IllegalArgumentException] if [length] is less than `0`.
 * @throws[IllegalArgumentException] if [length] is greater than `4`.
 * @throws[IndexOutOfBoundsException] if [offset] is less than `0`.
 * @throws[IndexOutOfBoundsException] if [offset] + [length] is greater than the array's
 * [lastIndex].
 */
internal fun ByteArray.readArbitrarySizeInt(
    length: Int,
    endian: Endianness,
    offset: Int = 0,
): Int {
    checkArbitraryLength(length)
    if (offset < 0) {
        throw IndexOutOfBoundsException("offset < 0 (size=$size)")
    } else if (offset + length > lastIndex) {
        throw IndexOutOfBoundsException("$offset is too high (size=$size)")
    }

    var result = 0

    for (i in 0 until length) {
        val bytesToShift =
            if (endian == Endianness.LITTLE) i
            else length - i - 1

        val bits = this[offset + i].toInt()
        result = result or (bits shl (Byte.SIZE_BITS * bytesToShift))
    }

    return result
}

/**
 * @throws[IllegalArgumentException] if the [length] is negative, or if it is too many bytes to hold
 * in a single [Int].
 */
private fun checkArbitraryLength(length: Int) {
    require(length >= 0) { "Int cannot use a negative number of bytes: $length" }
    require(length <= SIZE_BYTES) { "Int can only hold up to $SIZE_BYTES bytes, not $length" }
}