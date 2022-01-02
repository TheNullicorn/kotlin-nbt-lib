package me.nullicorn.ooze.nbt.testutil.data

import me.nullicorn.ooze.nbt.Type
import me.nullicorn.ooze.nbt.Type.*
import me.nullicorn.ooze.nbt.testutil.data.values.*

object Values {

    fun oneOf(type: Type): Any = forType(type).first()

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