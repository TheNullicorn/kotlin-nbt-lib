package me.nullicorn.ooze.nbt.io

import me.nullicorn.ooze.nbt.io.source.EmptyInputSource
import me.nullicorn.ooze.nbt.io.source.StreamInputSource
import me.nullicorn.ooze.nbt.lib.isExhausted
import java.io.IOException
import java.io.InputStream
import java.io.PushbackInputStream
import java.util.zip.GZIPInputStream
import java.util.zip.InflaterInputStream

/**
 * Constructs a new NBT reader whose data comes from an input [stream].
 *
 * Standard compression for NBT is handled automatically (ZLIB and gzip).
 *
 * This function does not close the supplied [stream]. That is the responsibility of the caller,
 * which should only do so after the desired NBT has finished being read by the returned [NbtInput].
 *
 * @param[stream] A stream supplying (possibly compressed) binary NBT data. The stream itself may be
 * empty.
 * @return a reader for the NBT data supplied by the [stream].
 *
 * @throws[InputException] if an I/O related error occurs while determining the stream's compression
 * type.
 */
fun NbtInput(stream: InputStream): NbtInput {
    // Use the empty stream instance if no bytes are available.
    if (stream.isExhausted) return NbtInput(EmptyInputSource)

    // If the stream already has a decompressor, we have nothing else to do here.
    if (stream is GZIPInputStream || stream is InflaterInputStream) {
        return NbtInput(StreamInputSource(stream))
    }

    /*
     * The goal of the next code is to read the stream's first 2 bytes (aka "magic numbers") to
     * determine if it's compressed (and what type, if applicable).
     *
     * Each I/O operation has its own try/catch so different issues can be differentiated between
     * when debugging.
     */

    // Allows us to "peek" at the first two bytes to see if they're a GZIP header, then un-read
    // them when we're done.
    val peekStream = PushbackInputStream(stream, 2)

    // Peek at the first magic byte, which may indicate compression.
    val magic1 = try {
        peekStream.read().toByte()
    } catch (cause: IOException) {
        throw InputException("Failed to check stream compression (first magic byte)", cause)
    }

    // We need two magic bytes to proceed, so exit if there isn't a second.
    if (stream.isExhausted) try {
        // Make sure to return the peeked byte to the stream.
        peekStream.unread(1)
        // Return the 1-byte stream (possible if the only tag is a TAG_End, indicating empty data).
        return NbtInput(StreamInputSource(stream))
    } catch (cause: IOException) {
        throw InputException("Failed to return first magic byte to the stream", cause)
    }

    // Peek at the second magic byte, which may indicate compression.
    val magic2 = try {
        peekStream.read().toByte()
    } catch (cause: IOException) {
        throw InputException("Failed to check stream compression (second magic byte)", cause)
    }

    // Make sure to return both peeked bytes to the stream.
    try {
        peekStream.unread(2)
    } catch (cause: IOException) {
        throw InputException("Failed to return both magic bytes to the stream", cause)
    }

    // Wrap the stream in a decompressed one, if necessary.
    val decompressedStream = when (NbtCompression.fromMagicNumbers(magic1, magic2)) {
        NbtCompression.GZIP -> GZIPInputStream(peekStream)
        NbtCompression.ZLIB -> InflaterInputStream(peekStream)
        NbtCompression.NONE -> peekStream
    }

    return NbtInput(StreamInputSource(decompressedStream))
}