package me.nullicorn.ooze.nbt.io.codec

import me.nullicorn.ooze.nbt.io.InputException
import me.nullicorn.ooze.nbt.io.runUnsafeInput
import me.nullicorn.ooze.nbt.io.source.InputSource

/**
 * Consumes a dynamic number of bytes from the source, interpreted as a
 * [modified](https://docs.oracle.com/javase/8/docs/api/java/io/DataInput.html#modified-utf-8)
 * UTF-8 string.
 *
 * This will always consume at least `2` bytes if no exception is thrown.
 *
 * @return the UTF-8 string decoded from the consumed bytes.
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/io/DataInput.html#readUTF--">
 *     Modified UTF-8</a> Encoding Details
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * consume, or if it becomes exhausted during the operation.
 */
internal fun InputSource.readString(): String {
    val utfLength = runUnsafeInput("reading UTF length") { readShort().toUShort().toInt() }
    val utfBytes = runUnsafeInput("reading UTF value") { readToNewBuffer(utfLength) }

    // utfLength is just the max length. The whole array might not be used.
    val chars = CharArray(utfLength)

    var byteIndex = 0
    var charIndex = 0

    while (byteIndex < utfLength) {
        val byte1 = utfBytes[byteIndex++].toInt()
        chars[charIndex++] = when (byte1 ushr 4) {
            0, 1, 2, 3, 4, 5, 6, 7 -> {
                // Use the byte's actual value as the character.
                byte1.toChar()
            }

            12, 13 -> {
                val byte2 = utfBytes[byteIndex++].toInt()

                // Second byte's most-sig-bits must be 1 and 0.
                if (byte2 and 0b11000000 != 0b10000000) {
                    throw InputException("Second byte of group $charIndex has the wrong MSBs")
                }

                // Combine the first byte's lowest 5 bits with the second byte's lowest 6.
                Char((byte1 and 0b11111 shl 6) or (byte2 and 0b111111))
            }

            14 -> {
                val byte2 = utfBytes[byteIndex++].toInt()
                val byte3 = utfBytes[byteIndex++].toInt()

                // Second and third byte's most-sig-bits must both be 1 and 0.
                if (byte2 and 0b11000000 != 0b10000000) {
                    throw InputException("Second byte of group $charIndex has the wrong MSBs")
                }

                if (byte3 and 0b11000000 != 0b10000000) {
                    throw InputException("Third byte of group $charIndex has the wrong MSBs")
                }

                // Combine the first byte's lowest 4 bits with the second and third bytes'
                // lowest 6 bits.
                Char((byte1 and 0b1111 shl 12)
                        or (byte2 and 0b111111 shl 6)
                        or (byte3 and 0b111111))
            }

            else -> throw InputException("First byte of group $charIndex has the wrong MSBs")
        }
    }

    return chars.concatToString(endIndex = charIndex)
}

/**
 * Discards a dynamic number bytes from the source, depending on the length of the next
 * [modified UTF-8][readString] string supplied by the source.
 *
 * This will always discard at least `2` bytes if no exception is thrown.
 *
 * @throws[InputException] if the source is already exhausted, and there are no more bytes to
 * discard, or if it becomes exhausted during the operation.
 */
internal fun InputSource.skipString() {
    val utfLength = runUnsafeInput("reading UTF length") { readShort().toUShort().toInt() }
    runUnsafeInput("skipping UTF value") { skip(utfLength) }
}