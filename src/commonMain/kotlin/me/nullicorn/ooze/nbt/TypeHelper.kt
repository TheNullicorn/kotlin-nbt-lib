package me.nullicorn.ooze.nbt

import me.nullicorn.ooze.nbt.Type.*

/*
 * The "multiplatform" dev build that adds JS enum support has some trouble with internal properties
 * on enums. Until that's fixed, this extension property is the workaround.
 *
 * In this case "trouble" means that all code accessing the property references the non-obfuscated
 * name ("runtimeType"), but the property itself compiles to an obfuscated name
 * ("_get_runtimeType__3579803807"). So when something tries to access it, it uses the wrong name
 * and just gets `undefined`.
 *
 * As soon as possible, this should be made a constructor of [Type].
 *
 * Pending issue [KT-50504]: https://youtrack.jetbrains.com/issue/KT-50504
 */

/**
 * The class to be used by values of the current type.
 */
internal val Type.runtimeType
    get() = when (this) {
        BYTE -> Byte::class
        SHORT -> Short::class
        INT -> Int::class
        LONG -> Long::class
        FLOAT -> Float::class
        DOUBLE -> Double::class
        BYTE_ARRAY -> ByteArray::class
        INT_ARRAY -> IntArray::class
        LONG_ARRAY -> LongArray::class
        STRING -> String::class
        LIST -> TagList::class
        COMPOUND -> TagCompound::class
    }

/**
 * Casts or converts the current value to a valid runtime object for an NBT [Type].
 *
 * If the current value is a [Number] it can be converted to a value for any numeric NBT type.
 * Numeric types are:
 * - [Type.BYTE] as [kotlin.Byte]
 * - [Type.SHORT] as [kotlin.Short]
 * - [Type.INT] as [kotlin.Int]
 * - [Type.LONG] as [kotlin.Long]
 * - [Type.FLOAT] as [kotlin.Float]
 * - [Type.DOUBLE] as [kotlin.Double]
 * If the current number is already the correct type for the NBT type, the value itself is returned.
 *
 * If the value is a [ByteArray], [IntArray], or [LongArray], it can be converted to any other NBT
 * array type. Array types are:
 * - [Type.BYTE_ARRAY] as [kotlin.ByteArray]
 * - [Type.INT_ARRAY] as [kotlin.IntArray]
 * - [Type.LONG_ARRAY] as [kotlin.LongArray]
 * If the current array is already the correct type for the NBT type, the array itself is returned.
 *
 * If the value is a [String], [TagList], or [TagCompound], the value cannot be converted, and
 * **must** already be the correct class. These types are:
 * - [Type.STRING] as [kotlin.String]
 * - [Type.LIST] as [TagList]
 * - [Type.COMPOUND] as [TagCompound]
 *
 * @throws[IllegalStateException] if the current value is not the correct class for the NBT [type],
 * and no conersion is available.
 */
internal inline infix fun <reified T> Any.asNbt(type: Type): T = when (this) {
    /*
     * Implicitly convert numeric types between one another, if necessary.
     */

    is Number -> when (type) {
        BYTE -> this as? Byte ?: toByte()
        SHORT -> this as? Short ?: toShort()
        INT -> this as? Int ?: toInt()
        LONG -> this as? Long ?: toLong()
        FLOAT -> this as? Float ?: toFloat()
        DOUBLE -> this as? Double ?: toDouble()
        else -> throw this isWrongFor type
    }

    /*
     * Convert between array types, if necessary.
     */

    is ByteArray -> when (type) {
        BYTE_ARRAY -> this
        INT_ARRAY -> IntArray(size) { i -> this[i].toInt() }
        LONG_ARRAY -> LongArray(size) { i -> this[i].toLong() }
        else -> throw this isWrongFor type
    }

    is IntArray -> when (type) {
        BYTE_ARRAY -> ByteArray(size) { i -> this[i].toByte() }
        INT_ARRAY -> this
        LONG_ARRAY -> LongArray(size) { i -> this[i].toLong() }
        else -> throw this isWrongFor type
    }

    is LongArray -> when (type) {
        BYTE_ARRAY -> ByteArray(size) { i -> this[i].toByte() }
        INT_ARRAY -> IntArray(size) { i -> this[i].toInt() }
        LONG_ARRAY -> this
        else -> throw this isWrongFor type
    }

    /*
     * Types that aren't numbers or arrays must strictly match the Type's runtimeType.
     */

    else -> {
        if (type.runtimeType.isInstance(this)) this
        else throw this isWrongFor type
    }
} as T

private infix fun Any.isWrongFor(type: Type) =
    IllegalStateException("Expected ${type.runtimeType} for $type, not ${this::class}")