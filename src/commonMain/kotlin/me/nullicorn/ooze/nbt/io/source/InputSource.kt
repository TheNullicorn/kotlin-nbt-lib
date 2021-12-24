package me.nullicorn.ooze.nbt.io.source

import me.nullicorn.ooze.nbt.io.InputException
import me.nullicorn.ooze.nbt.io.runUnsafeInput

/**
 * Provides a sequence of bytes to a consumer, which can decide to [consume][readToBuffer] or
 * [discard][skip] those bytes.
 */
internal interface InputSource {

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

    private companion object {
        private const val MAX_ALLOCATION = Int.SIZE_BITS - 8
    }
}