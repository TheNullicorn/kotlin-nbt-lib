package me.nullicorn.ooze.nbt.test.data

import me.nullicorn.ooze.nbt.Type
import me.nullicorn.ooze.nbt.Type.*
import me.nullicorn.ooze.nbt.test.convert
import me.nullicorn.ooze.nbt.test.data.values.*

/**
 * Data for testing values of NBT tags.
 */
object Values {

    /**
     * @return a valid value of the supplied [type]. This will **never** be a value that needs to be
     * [converted][convert] implicitly.
     */
    fun oneOf(type: Type): Any = forType(type).first()

    /**
     * @return a sequence of valid values for the supplied [type]. This will **never** include any
     * values that need to be [converted][convert] implicitly.
     */
    fun forType(type: Type): Set<Any> = when (type) {
        BYTE -> byteValues
        SHORT -> shortValues
        INT -> intValues
        LONG -> longValues
        FLOAT -> floatValues
        DOUBLE -> doubleValues
        BYTE_ARRAY -> byteArrayValues
        STRING -> stringValues
        LIST -> listValues()
        COMPOUND -> compoundValues()
        INT_ARRAY -> intArrayValues
        LONG_ARRAY -> longArrayValues
    }.toSet()
}