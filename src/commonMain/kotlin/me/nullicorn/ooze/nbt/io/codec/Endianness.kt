package me.nullicorn.ooze.nbt.io.codec

/**
 * The order to combine bytes in when reading an integer or floating-point number from a stream.
 */
internal enum class Endianness {
    /**
     * Indicates that the least-significant (lower place value) bytes will come first in a stream or
     * a byte sequence
     */
    LITTLE,

    /**
     * Indicates that the most-significant (higher place value) bytes will come first in a stream or
     * a byte sequence.
     */
    BIG
}