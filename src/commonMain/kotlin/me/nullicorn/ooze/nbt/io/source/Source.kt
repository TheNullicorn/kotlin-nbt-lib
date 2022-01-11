package me.nullicorn.ooze.nbt.io.source

import me.nullicorn.ooze.nbt.io.InputException
import me.nullicorn.ooze.nbt.io.Endianness
import me.nullicorn.ooze.nbt.io.codec.readSmallInt
import me.nullicorn.ooze.nbt.io.codec.readLargeInt
import me.nullicorn.ooze.nbt.io.runUnsafeInput

/**
 * Provides a sequence of bytes to a consumer, which can decide to [consume][readToBuffer] or
 * [discard][skip] those bytes.
 */
internal interface Source {

    /**
     * Consumes the next `n` bytes from the source, copying them to a new array.
     *
     * @param[length] `n`, the number of bytes to consume
     * @return a new buffer containing the copied bytes.
     *
     * @throws[InputException] if the source cannot be read from, or if the source runs out of bytes
     * to consume before the operation is complete.
     */
    fun readToNewBuffer(length: Int): ByteArray {
        require(length <= MAX_ALLOCATION) {
            "Cannot allocate more than $MAX_ALLOCATION bytes ($length)"
        }

        return readToBuffer(ByteArray(length), 0, length)
    }

    /**
     * Consumes the next `n` bytes from the source, copying them to an existing [buffer] starting at
     * a given [offset] index.
     *
     * @param[buffer] The array to copy the consumed bytes to.
     * @param[offset] The index in the [buffer] that the consumed bytes should start being copied
     * to.
     * @param[length] `n`, the number of bytes to consume
     * @return the [buffer] instance supplied.
     *
     * @throws[InputException] if the source cannot be read from, or if the source runs out of bytes
     * to consume before the operation is complete.
     */
    fun readToBuffer(buffer: ByteArray, offset: Int, length: Int): ByteArray

    /**
     * Discards the next `n` bytes from the source.
     *
     * @param[length] The number of bytes to discard.
     *
     * @throws[InputException] if the source cannot be read from, or if the source runs out of bytes
     * to discard before `n` bytes have been discarded.
     */
    fun skip(length: Int)

    /**
     * Consumes the next byte from the source.
     *
     * @return the value of the consumed byte, as an integer.
     *
     * @throws[InputException] if the source is already exhausted, and there are no more bytes to
     * consume.
     */
    fun readByte(): Byte

    /**
     * Discards the next byte from the source.
     *
     * @throws[InputException] if the source is already exhausted, and there are no more bytes to
     * discard.
     */
    fun skipByte() = runUnsafeInput("skipping byte") { skip(1) }

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
    fun readShort(endian: Endianness = Endianness.BIG) = runUnsafeInput("reading short") {
        readSmallInt(length = Short.SIZE_BYTES, endian).toShort()
    }

    /**
     * Discards the next `2` bytes from the source.
     *
     * @throws[InputException] if the source is already exhausted, and there are no more bytes to
     * discard, or if it becomes exhausted during the operation.
     */
    fun skipShort() = runUnsafeInput("skipping short") { skip(length = Short.SIZE_BYTES) }

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
    fun readInt(endian: Endianness = Endianness.BIG) = runUnsafeInput("reading int") {
        readSmallInt(length = Int.SIZE_BYTES, endian)
    }

    /**
     * Discards the next `4` bytes from the source.
     *
     * @throws[InputException] if the source is already exhausted, and there are no more bytes to
     * discard, or if it becomes exhausted during the operation.
     */
    fun skipInt() = runUnsafeInput("skipping int") { skip(length = Int.SIZE_BYTES) }

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
    fun readLong(endian: Endianness = Endianness.BIG) = runUnsafeInput("reading long") {
        readLargeInt(length = Long.SIZE_BYTES, endian)
    }

    /**
     * Discards the next `8` bytes from the source.
     *
     * @throws[InputException] if the source is already exhausted, and there are no more bytes to
     * discard, or if it becomes exhausted during the operation.
     */
    fun skipLong() = runUnsafeInput("skipping long") { skip(Long.SIZE_BYTES) }

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
    fun readFloat(endian: Endianness = Endianness.BIG) = runUnsafeInput("reading float") {
        Float.fromBits(readSmallInt(length = Long.SIZE_BYTES, endian))
    }

    /**
     * Discards the next `4` bytes from the source.
     *
     * @throws[InputException] if the source is already exhausted, and there are no more bytes to
     * discard, or if it becomes exhausted during the operation.
     */
    fun Source.skipFloat() = runUnsafeInput("skipping float") { skip(Long.SIZE_BYTES) }

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
    fun readDouble(endian: Endianness = Endianness.BIG) = runUnsafeInput("reading double") {
        Double.fromBits(readLargeInt(length = Double.SIZE_BYTES, endian))
    }

    /**
     * Discards the next `8` bytes from the source.
     *
     * @throws[InputException] if the source is already exhausted, and there are no more bytes to
     * discard, or if it becomes exhausted during the operation.
     */
    fun skipDouble() = runUnsafeInput("skipping double") { skip(Double.SIZE_BYTES) }

    /**
     * Consumes `4` bytes for each `32`-bit int in an array with the supplied [length].
     *
     * @param[length] The number of `4`-byte ints to read from the stream.
     * @param[endian] The order to combine each int's bytes in. This does not affect the order in which
     * the ints themselves are read.
     *
     * @throws[InputException] if the source is already exhausted, and there are no more bytes to
     * consume, or if it becomes exhausted during the operation.
     */
    fun readIntArray(length: Int, endian: Endianness = Endianness.BIG): IntArray {
        require(length >= 0) { "Array length cannot be negative ($length)" }

        val bytes = runUnsafeInput("reading int array") {
            readToNewBuffer(length * Int.SIZE_BYTES)
        }

        return IntArray(length) { index ->
            bytes.readSmallInt(Int.SIZE_BYTES, endian, offset = index * Int.SIZE_BYTES)
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
    fun skipIntArray(length: Int) {
        require(length >= 0) { "Array length cannot be negative ($length)" }

        runUnsafeInput("skipping int array") {
            skip(length * Int.SIZE_BYTES)
        }
    }

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
    fun readLongArray(
        length: Int,
        endian: Endianness = Endianness.BIG,
    ): LongArray {
        require(length >= 0) { "Array length cannot be negative ($length)" }

        val bytes = runUnsafeInput("reading long array") {
            readToNewBuffer(length * Long.SIZE_BYTES)
        }

        return LongArray(length) { index ->
            bytes.readLargeInt(Long.SIZE_BYTES, endian, offset = index * Long.SIZE_BYTES)
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
    fun Source.skipLongArray(length: Int) {
        require(length >= 0) { "Array length cannot be negative ($length)" }

        runUnsafeInput("skipping long array") {
            skip(length * Long.SIZE_BYTES)
        }
    }

    private companion object {
        private const val MAX_ALLOCATION = Int.MAX_VALUE - 8
    }
}