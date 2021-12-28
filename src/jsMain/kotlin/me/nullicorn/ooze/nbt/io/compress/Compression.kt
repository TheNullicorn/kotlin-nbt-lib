// Suppressed so we can cast between ByteArray and Uint8Array without warnings.
//
// Pako expects a Uint8Array, which is what ByteArrays are under the hood, but
// the IDE doesn't recognize that they're equivalent.
@file:Suppress("CAST_NEVER_SUCCEEDS")

package me.nullicorn.ooze.nbt.io.compress

import me.nullicorn.ooze.nbt.io.InputException
import org.khronos.webgl.Uint8Array

internal actual fun ByteArray.compress(method: Method): ByteArray = when (method) {
    Method.NONE -> this

    Method.ZLIB -> try {
        Pako.deflate(this as Uint8Array) as ByteArray
    } catch (cause: Throwable) {
        TODO("Throw OutputException (not such exception yet)")
    }

    Method.GZIP -> try {
        Pako.gzip(this as Uint8Array) as ByteArray
    } catch (cause: Throwable) {
        TODO("Throw OutputException (not such exception yet)")
    }
}

internal actual fun ByteArray.decompress(method: Method): ByteArray = when (method) {
    Method.NONE -> this

    Method.ZLIB -> try {
        Pako.inflate(this as Uint8Array) as ByteArray
    } catch (cause: Throwable) {
        throw InputException("Failed to decompress deflated array")
    }

    Method.GZIP -> try {
        Pako.ungzip(this as Uint8Array) as ByteArray
    } catch (cause: Throwable) {
        throw InputException("Failed to decompress gzipped array")
    }
}