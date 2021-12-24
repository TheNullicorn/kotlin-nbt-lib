package me.nullicorn.ooze.nbt

import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

/**
 * Standard data representations that can be understood by NBT parsers.
 *
 * @param[identifier] The unique value used to differentiate the type from others, when encoded.
 */
@JsExport
enum class Type(val identifier: Byte) {
    /**
     * An 8-bit signed integer.
     *
     * Also known as an octet.
     */
    BYTE(1),

    /**
     * A 16-bit signed integer.
     */
    SHORT(2),

    /**
     * A 32-bit signed integer.
     */
    INT(3),

    /**
     * A 64-bit signed integer.
     */
    LONG(4),

    /**
     * A single precision, floating point number (32 bits), represented according to IEEE 754.
     */
    FLOAT(5),

    /**
     * A double precision, floating point number (64 bits), represented according to IEEE 754.
     */
    DOUBLE(6),

    /**
     * An ordered sequence of signed bytes (octets).
     */
    BYTE_ARRAY(7),

    /**
     * An ordered sequence of 32-bit signed integers.
     */
    INT_ARRAY(11),

    /**
     * An ordered sequence of 64-bit signed integers.
     */
    LONG_ARRAY(12),

    /**
     * A sequence of UTF-8 characters.
     */
    STRING(8),

    /**
     * An ordered collection of NBT tags, all of which share the same [Type].
     */
    LIST(9),

    /**
     * An unordered collection of NBT tags mapped to unique names (UTF-8 strings). Each tag in a
     * compound can have any [Type].
     */
    COMPOUND(10);

    // Converts the name() to the "type name" as seen on https://wiki.vg/NBT:
    // - TAG_Byte for BYTE
    // - TAG_Long_Array for LONG_ARRAY
    // - etc
    override fun toString(): String {
        var result = "TAG"
        for (word in name.split('_')) {
            result += "_" + word[0].uppercaseChar() + word.substring(1).lowercase()
        }
        return result
    }

    companion object Lookup {
        // Reusable result of Type.values(), so that we don't allocate a new array each time.
        @JvmStatic
        private val values: Array<Type> by lazy(Type::values)

        /**
         * Retrieve a [Type] by its unique [identifier][Type.identifier].
         *
         * @param[identifier] The identifier to lookup.
         * @return the type that uses the [identifier], or `null` if no type does.
         */
        @JvmStatic
        fun fromIdentifier(identifier: Byte) = values.firstOrNull {
            it.identifier == identifier
        }
    }
}