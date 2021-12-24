package me.nullicorn.ooze.nbt.io.source

import me.nullicorn.ooze.nbt.io.InputException

/**
 * A source whose information is provided by a fully-present sequence of bytes in memory.
 *
 * The supplied [source] is copied upon construction to avoid tampering.
 *
 * @param[source] The sequence of bytes to be consumed.
 */
internal class ByteArrayInputSource(source: ByteArray) : InputSource {

    private val source = source.copyOf()

    /**
     * The index in the [source] where the next byte can be found.
     *
     * This increases by `1` for every byte read.
     */
    private var head = 0

    override fun readToBuffer(buffer: ByteArray, offset: Int, length: Int): ByteArray {
        // Make sure the supplied array has enough room for the bytes.
        if (offset + length > buffer.size) throw IndexOutOfBoundsException(
            "Input buffer overflowed(offset $offset + length $length >= buffer.size ${buffer.size})"
        )

        // Make sure we have enough bytes left to give.
        if (outOfBytes(length)) throw InputException(
            "Not enough bytes in source array to copy $length to buffer"
        )

        source.copyInto(
            destination = buffer,
            destinationOffset = offset,
            startIndex = head,
            endIndex = head + length
        )
        head += length

        return buffer
    }

    override fun skip(length: Int) {
        if (outOfBytes(length)) {
            throw InputException("Not enough bytes in source array to skip $length bytes")
        }
        head++
    }

    override fun readByte(): Byte {
        if (outOfBytes(1)) throw InputException("No more bytes in source array")
        return source[head++]
    }

    /**
     * Internal helper for checking if the end of the array has been reached.
     *
     * @param[amount] The number of bytes required to be left by the caller.
     * @return `true` if the [source] does not have enough bytes left after the [head] to supply
     * the required [amount] of bytes. `false` if there are enough bytes left.
     */
    private fun outOfBytes(amount: Int) = (head + amount > source.size)
}