package me.nullicorn.ooze.nbt.io.compress

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.DeflaterInputStream
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

internal actual fun ByteArray.compress(method: Method): ByteArray {
    // Nothing to do if no method is selected.
    if (method == Method.NONE) return this

    return try {
        // Create a destination for the compressed.
        // (initial capacity is 1/3 of the uncompressed size)
        val destination = ByteArrayOutputStream(size / 3)

        // Wrap the array in a gzip or ZLIB compressor, depending on the method.
        val compressor = when (method) {
            Method.GZIP -> GZIPOutputStream(destination)
            else -> DeflaterOutputStream(destination)
        }

        // Compress the entire stream, close it.
        compressor.use { compressor.write(this) }

        // Return the compressed array.
        destination.toByteArray()

    } catch (cause: IOException) {
        TODO("Throw OutputException (no such exception yet)")
    }
}

internal actual fun ByteArray.decompress(method: Method): ByteArray {
    // Nothing to do if no method is selected.
    if (method == Method.NONE) return this

    return try {
        // Wrap the array in a gzip or ZLIB decompressor, depending on the method.
        val source = ByteArrayInputStream(this)
        val compressor = when (method) {
            Method.GZIP -> GZIPInputStream(source)
            else -> DeflaterInputStream(source)
        }

        // Decompress the entire stream, close it, then return the result.
        compressor.use { compressor.readBytes() }

    } catch (cause: IOException) {
        TODO("Throw OutputException (no such exception yet)")
    }
}