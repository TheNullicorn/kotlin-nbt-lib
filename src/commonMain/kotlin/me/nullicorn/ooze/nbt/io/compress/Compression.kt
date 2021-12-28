package me.nullicorn.ooze.nbt.io.compress

import me.nullicorn.ooze.nbt.io.InputException

/**
 * Creates a copy of the array, compressed using a specified [method].
 *
 * If the [method] is [NONE][Method.NONE], the array itself is returned, unmodified. Otherwise, a
 * new array is created to hold the compressed bytes.
 *
 * @param[method] The type of compression to use.
 * @return a new array containing the compressed contents of the array.
 *
 * @throws[OutputException] if the array fails to compress.
 */
internal expect fun ByteArray.compress(method: Method): ByteArray

/**
 * Decompresses the contents of the array, compressed using a specified [method].
 *
 * If the [method] is [NONE][Method.NONE], the array itself is returned, unmodified. Otherwise, a
 * new array is created to hold the decompressed bytes.
 *
 * @throws[InputException] if the array fails to decompress.
 */
internal expect fun ByteArray.decompress(method: Method): ByteArray