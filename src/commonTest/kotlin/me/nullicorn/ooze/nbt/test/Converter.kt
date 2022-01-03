package me.nullicorn.ooze.nbt.test

import me.nullicorn.ooze.nbt.TagCompound
import me.nullicorn.ooze.nbt.TagList
import me.nullicorn.ooze.nbt.Type
import me.nullicorn.ooze.nbt.Type.*

/**
 * Converts a [value][value] [from][Convertable.from] one NBT type [to][Converter.to] another.
 *
 * The function is intended to be used with two other infix functions like so:
 * ```kotlin
 * convert(value) from typeA to typeB
 * ```
 * Where...
 * - `value` is the object to convert
 * - `typeA` is the [value]'s current NBT [Type] that the value is intended for
 * - `typeB` is the NBT [Type] that the value should be converted to be compatible with
 *
 *
 * @param[value]
 */
fun convert(value: Any) = Convertable(value)

data class Convertable(private val value: Any) {
    infix fun from(currentType: Type) = Converter(value, currentType)
}

data class Converter(private val value: Any, private val currentType: Type) {
    infix fun to(targetType: Type) = when (value) {
        is Number -> when (targetType) {
            BYTE -> value.toByte()
            SHORT -> value.toShort()
            INT -> value.toInt()
            LONG -> value.toLong()
            FLOAT -> value.toFloat()
            DOUBLE -> value.toDouble()
            else -> null
        }

        is ByteArray -> when (targetType) {
            BYTE_ARRAY -> value
            INT_ARRAY -> IntArray(value.size) { i -> value[i].toInt() }
            LONG_ARRAY -> LongArray(value.size) { i -> value[i].toLong() }
            else -> null
        }

        is IntArray -> when (targetType) {
            BYTE_ARRAY -> ByteArray(value.size) { i -> value[i].toByte() }
            INT_ARRAY -> value
            LONG_ARRAY -> LongArray(value.size) { i -> value[i].toLong() }
            else -> null
        }

        is LongArray -> when (targetType) {
            BYTE_ARRAY -> ByteArray(value.size) { i -> value[i].toByte() }
            INT_ARRAY -> IntArray(value.size) { i -> value[i].toInt() }
            LONG_ARRAY -> value
            else -> null
        }

        is String -> when (targetType) {
            STRING -> value
            else -> null
        }

        is TagList -> when (targetType) {
            LIST -> value
            else -> null
        }

        is TagCompound -> when (targetType) {
            COMPOUND -> value
            else -> null
        }

        else -> null

    } ?: throw IllegalStateException("Cannot convert $currentType to $targetType")
}