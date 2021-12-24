package me.nullicorn.ooze.nbt.lib

import me.nullicorn.ooze.nbt.io.InputException
import java.io.IOException
import java.io.InputStream

/**
 * Indicates that the stream has run out (or never had) any bytes left to [read][InputStream.read]
 * or [skip][InputStream.skip].
 *
 * This is equivalent to [available()][InputStream.available]` <= 0`, while wrapping any
 * [IOException]s in [InputException].
 *
 * @throws[InputStream] if an I/O exception occurs while checking the stream's remaining bytes.
 */
internal val InputStream.isExhausted
    get() = try {
        available() <= 0
    } catch (cause: IOException) {
        throw InputException("Failed to determine stream state", cause)
    }