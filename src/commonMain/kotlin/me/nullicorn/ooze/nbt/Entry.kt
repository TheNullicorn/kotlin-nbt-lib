package me.nullicorn.ooze.nbt

import kotlin.js.JsExport

/**
 * An NBT [value] associated with a [name] inside an NBT [compound][TagCompound].
 *
 * @param[type] The NBT type used to represent the value.
 * @param[name] The unique identifier used to lookup the value.
 * @param[value] The contents of the tag.
 *
 * @throws[IllegalArgumentException] if the [value]'s class is incorrect, given the supplied [type].
 */
@JsExport
class Entry(
    val type: Type,
    val name: String,
    value: Any,
) {
    /**
     * The contents of the tag.
     */
    val value: Any

    init {
        this.value = try {
            value asNbt type
        } catch (cause: IllegalStateException) {
            throw IllegalArgumentException("Entry value is not a $type: $value", cause)
        }
    }

    /**
     * The entry's [value], cast to a byte.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.BYTE].
     */
    val asByte get() = asType<Byte>(Type.BYTE)

    /**
     * The entry's [value], cast to a short.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.SHORT].
     */
    val asShort get() = asType<Short>(Type.SHORT)

    /**
     * The entry's [value], cast to an int.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.INT].
     */
    val asInt get() = asType<Int>(Type.INT)

    /**
     * The entry's [value], cast to a long.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.LONG].
     */
    val asLong get() = asType<Long>(Type.LONG)

    /**
     * The entry's [value], cast to a float.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.FLOAT].
     */
    val asFloat get() = asType<Float>(Type.FLOAT)

    /**
     * The entry's [value], cast to a double.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.DOUBLE].
     */
    val asDouble get() = asType<Double>(Type.DOUBLE)

    /**
     * The entry's [value], cast to a byte array.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.BYTE_ARRAY].
     */
    val asByteArray get() = asType<ByteArray>(Type.BYTE_ARRAY)

    /**
     * The entry's [value], cast to an int array.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.INT_ARRAY].
     */
    val asIntArray get() = asType<IntArray>(Type.INT_ARRAY)

    /**
     * The entry's [value], cast to a long array.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.LONG_ARRAY].
     */
    val asLongArray get() = asType<LongArray>(Type.LONG_ARRAY)

    /**
     * The entry's [value], cast to a string.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.STRING].
     */
    val asString get() = asType<String>(Type.STRING)

    /**
     * The entry's [value], cast to a list.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.LIST].
     */
    val asList get() = asType<TagList>(Type.LIST)

    /**
     * The entry's [value], cast to a compound.
     *
     * @throws[IllegalStateException] if the entry's [type] is not [Type.COMPOUND].
     */
    val asCompound get() = asType<TagCompound>(Type.COMPOUND)

    ///////////////////////////////////////////////////////////////////////////
    // Destructuring Functions
    //
    // e.g. allows for: (name, type, value) = Entry("", Type.BYTE, 0)
    ///////////////////////////////////////////////////////////////////////////

    /**
     * The entry's [name].
     */
    operator fun component1() = name

    /**
     * The entry's [type]
     */
    operator fun component2() = type

    /**
     * The entry's [value].
     */
    operator fun component3() = value

    private inline fun <reified T> asType(type: Type): T {
        check(this.type == type) {
            "\"$name\" tag is ${this.type}, not $type"
        }
        return value as T
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Entry

        if (type != other.type) return false
        if (name != other.name) return false
        if (value != other.value) return when (type) {
            // Use array-specific equals methods if necessary.
            Type.BYTE_ARRAY -> (value as ByteArray).contentEquals(other.value as ByteArray)
            Type.INT_ARRAY -> (value as IntArray).contentEquals(other.value as IntArray)
            Type.LONG_ARRAY -> (value as LongArray).contentEquals(other.value as LongArray)
            // Otherwise, the outer check proves they aren't equal.
            else -> false
        }

        return true
    }

    override fun hashCode() = 31 * type.hashCode() + name.hashCode()
}
