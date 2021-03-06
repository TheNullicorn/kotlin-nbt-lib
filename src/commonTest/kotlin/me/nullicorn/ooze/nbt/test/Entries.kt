package me.nullicorn.ooze.nbt.test

import me.nullicorn.ooze.nbt.Entry
import me.nullicorn.ooze.nbt.Type
import me.nullicorn.ooze.nbt.Type.*

/**
 * Data for testing the [Entry] class.
 */
object Entries {

    /**
     * @return the type-specific getter property for an [Entry] with the supplied [type].
     */
    fun getterFor(type: Type): Entry.() -> Any = when (type) {
        BYTE -> Entry::asByte
        SHORT -> Entry::asShort
        INT -> Entry::asInt
        LONG -> Entry::asLong
        FLOAT -> Entry::asFloat
        DOUBLE -> Entry::asDouble
        BYTE_ARRAY -> Entry::asByteArray
        INT_ARRAY -> Entry::asIntArray
        LONG_ARRAY -> Entry::asLongArray
        STRING -> Entry::asString
        LIST -> Entry::asList
        COMPOUND -> Entry::asCompound
        else -> throw IllegalStateException("No known getter in Entry for ${type.name}")
    }
}