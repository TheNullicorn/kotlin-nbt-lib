@file:Suppress("NON_EXPORTABLE_TYPE")

package me.nullicorn.ooze.nbt.lib

/**
 * Utilities for creating [kotlin.Long] objects from JavaScript, which doesn't support 64-bit
 * numbers.
 */
@JsExport
object Long {
    private const val INT_SIZE = 32
    private const val INT_MASK = 0xFFFFFFFF.toInt()

    /**
     * Converts the [value] to a Kotlin Long, allowing it to be used with the rest of the library.
     */
    fun valueOf(value: Int) = value.toLong()

    /**
     * Combines the lowest `32`-bits of two values into a single kotlin Long (`64`-bit).
     *
     * @param[high] The most-significant bits of the long. If larger than 32-bits, it will be
     * truncated to fit.
     * @param[low] The least-significant bits of the long. If larger than 32-bits, it will be
     * truncated to fit.
     */
    fun fromBits(high: Int, low: Int): kotlin.Long {
        val safeHigh = high and INT_MASK shl INT_SIZE
        val safeLow = low and INT_MASK

        return safeHigh.toLong() or safeLow.toLong()
    }

    /**
     * Interprets the supplied [digits] as `64`-bit integer, in base-`10`.
     *
     * For other bases, see [fromRadixString].
     */
    fun fromString(digits: String) = digits.toLong()

    /**
     * Interprets the supplied [digits] as a `64`-bit integer, using the specified [radix].
     *
     * For base-`10`, [fromString] can be used.
     */
    fun fromRadixString(digits: String, radix: Int) = digits.toLong(radix)
}