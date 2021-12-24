package me.nullicorn.ooze.nbt.io.source

import me.nullicorn.ooze.nbt.io.InputException
import java.io.IOException
import java.io.InputStream

/**
 * A source whose information is provided on-demand by a Java [InputStream].
 *
 * The supplied [source] will not be closed automatically, so that must be done by the user once all
 * NBT data has been read.
 *
 * @param[source] The stream to provide binary data to the consumer.
 */
internal class StreamInputSource(private val source: InputStream) : InputSource {

    override fun readByte() = try {
        source.read().toByte()
    } catch (cause: IOException) {
        throw InputException("Failed to read byte from stream", cause)
    }

    override fun readToBuffer(buffer: ByteArray, offset: Int, length: Int): ByteArray {
        try {
            source.read(buffer, offset, length)
        } catch (cause: IOException) {
            throw InputException("Failed to read $length bytes from stream to buffer", cause)
        }

        return buffer
    }

    override fun skip(length: Int) {
        val numSkipped = try {
            source.skip(length.toLong())
        } catch (cause: IOException) {
            throw InputException("Failed to skip $length bytes in stream", cause)
        }

        if (numSkipped < length) {
            throw InputException("End of stream reached while skipping ($numSkipped/$length bytes)")
        }
    }
}