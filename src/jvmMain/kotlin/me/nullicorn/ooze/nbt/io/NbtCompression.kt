package me.nullicorn.ooze.nbt.io

/**
 * Supported methods for compressing binary NBT data.
 */
internal enum class NbtCompression {
    /**
     * Indicates the data is not compressed.
     */
    NONE,

    GZIP,

    ZLIB;

    companion object {
        // The bytes at the start of every stream using gzip.
        private const val GZIP_MAGIC = 0x1f8b

        // ZLIB magic numbers are always divisible by this.
        private const val ZLIB_FACTOR = 31

        // The highest allowed value for the ZLIB "CINFO" value (aka the window size).
        private const val ZLIB_MAX_WINDOW = 7

        // The ID of the only valid compression algorithm, DEFLATE.
        private const val ZLIB_DEFLATE_ALGORITHM = 8

        /**
         * Determines the type of compression used by a stream, based on the first two bytes in that
         * stream. Those bytes are also referred to as the stream's header, or its "magic numbers".
         *
         * Credit to @mwfearnley on Stack Overflow for breaking down the meaning of ZLIB's magic
         * numbers. See the attached link for their post.
         * @see <a href="https://stackoverflow.com/a/54915442">@mwfearnley's post</a>
         */
        fun fromMagicNumbers(byte1: Byte, byte2: Byte): NbtCompression {
            val magic = (byte1.toInt() shl 8) and byte2.toInt()

            // GZip uses a static magic number: 1F 8B
            if (magic == GZIP_MAGIC) return GZIP

            // ZLIB headers always divide cleanly by 31.
            if (magic % ZLIB_FACTOR == 0) {
                val windowSize = magic ushr 12 and 0b1111
                val algorithm = magic ushr 8 and 0b1111

                // In case the header is just coincidentally divisible by 31, also make sure the
                // compression window size and algorithm are valid values.
                if (windowSize <= ZLIB_MAX_WINDOW && algorithm == ZLIB_DEFLATE_ALGORITHM) {
                    return ZLIB
                }
            }

            // Otherwise, assume it's not compressed at all.
            return NONE
        }
    }
}