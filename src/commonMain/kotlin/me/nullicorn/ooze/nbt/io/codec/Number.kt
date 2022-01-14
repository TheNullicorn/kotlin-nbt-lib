package me.nullicorn.ooze.nbt.io.codec

import me.nullicorn.ooze.nbt.io.Endianness
import me.nullicorn.ooze.nbt.io.source.Source

private const val SIZE_BYTES = 4

/**
 * Used by [readSmallInt] so that the entire int can be read at once without allocating a
 * new array every time.
 */
private val buffer: ByteArray by lazy {
    ByteArray(maxOf(
        Byte.SIZE_BYTES,
        Short.SIZE_BYTES,
        Int.SIZE_BYTES,
        Long.SIZE_BYTES,
        Float.SIZE_BYTES,
        Double.SIZE_BYTES))
}

/**
 * Reads the next `n` bytes from the source, interpreting them as a signed, two's complement
 * integer.
 *
 * This function only supports untegers up to `4` bytes (`32` bits). For larger values, use
 * [readLargeInt].
 *
 * @param[length] The value of `n`; the number of bytes to read from the source.
 * @param[endian] The order in which to combine the bytes into an [Int].
 * @return the bytes, combined as a `32`-bit signed integer.
 * @see[readLargeInt]
 *
 * @throws[IllegalArgumentException] if [length] is less than `0`.
 * @throws[IllegalArgumentException] if [length] is greater than `8`.
 */
internal fun Source.readSmallInt(length: Int, endian: Endianness): Int {
    checkSmallIntLength(length)

    val bytes = readToBuffer(buffer, offset = 0, length)
    return bytes.readSmallInt(length, endian)
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
internal fun Source.readLargeInt(length: Int, endian: Endianness): Long {
    checkLargeIntLength(length)

    val bytes = readToBuffer(buffer, offset = 0, length)
    return bytes.readLargeInt(length, endian)
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
internal fun ByteArray.readSmallInt(
    length: Int,
    endian: Endianness,
    offset: Int = 0,
): Int {
    checkSmallIntLength(length)
    if (offset < 0) {
        throw IndexOutOfBoundsException("offset < 0 (size=$size)")
    } else if (offset + length > lastIndex) {
        throw IndexOutOfBoundsException("$offset is too high (size=$size)")
    }

    var result = 0

    for (i in 0 until length) {
        val bitsToShift = Byte.SIZE_BITS *
            if (endian == Endianness.LITTLE) i
            else length - i - 1

        val bits = this[offset + i].toInt() and 0xFF
        result = result or (bits shl bitsToShift)
    }

    return result
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
internal fun ByteArray.readLargeInt(
    length: Int,
    endian: Endianness,
    offset: Int = 0,
): Long {
    checkLargeIntLength(length)
    if (offset < 0) {
        throw IndexOutOfBoundsException("offset < 0 (size=$size)")
    } else if (offset + length > lastIndex) {
        throw IndexOutOfBoundsException("$offset is too high (size=$size)")
    }

    var result = 0L

    for (i in 0 until length) {
        val bitsToShift = Byte.SIZE_BITS *
            if (endian == Endianness.LITTLE) i
            else length - i - 1

        val bits = this[offset + i].toLong() and 0xFF
        result = result or (bits shl bitsToShift)
    }

    return result
}

/**
 * @throws[IllegalArgumentException] if the [length] is negative, or if it is too many bytes to hold
 * in a single [Int].
 */
private fun checkSmallIntLength(length: Int) {
    require(length >= 0) { "Int cannot use a negative number of bytes: $length" }
    require(length <= SIZE_BYTES) { "Int can only hold up to $SIZE_BYTES bytes, not $length" }
}

/**
 * @throws[IllegalArgumentException] if the [length] is negative, or if it is too many bytes to hold
 * in a single [Long].
 */
private fun checkLargeIntLength(length: Int) {
    require(length >= 0) { "Long cannot use a negative number of bytes: $length" }
    require(length <= SIZE_BYTES) { "Long can only hold up to $SIZE_BYTES bytes, not $length" }
}