package me.nullicorn.ooze.nbt.io.source

import me.nullicorn.ooze.nbt.io.InputException

/**
 * Represents a source of bytes with no data to provide.
 *
 * All read/skip operations throw [InputException]s, due to the lack of bytes to provide.
 */
internal object EmptySource : Source {
    override fun readToBuffer(buffer: ByteArray, offset: Int, length: Int): ByteArray {
        throw InputException("Source is empty; nothing to read")
    }

    override fun readByte(): Byte {
        throw InputException("Source is empty; nothing to read")
    }

    override fun skip(length: Int) {
        throw InputException("Source is empty; nothing to skip")
    }
}