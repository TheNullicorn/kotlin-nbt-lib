package me.nullicorn.ooze.nbt.testutil.data

import me.nullicorn.ooze.nbt.Type
import me.nullicorn.ooze.nbt.Type.*

object Types {
    val all: Set<Type>
        get() = Type.values().toSet()

    val numeric: Set<Type>
        get() = setOf(BYTE, SHORT, INT, LONG, FLOAT, DOUBLE)

    val nonNumeric: Set<Type>
        get() = Type.values()
            .toMutableSet()
            .apply { removeAll(numeric) }
            .toSet()

    val array: Set<Type>
        get() = setOf(BYTE_ARRAY, INT_ARRAY, LONG_ARRAY)

    val nonArray: Set<Type>
        get() = Type.values()
            .toMutableSet()
            .apply { removeAll(array) }
            .toSet()

    val strict: Set<Type>
        get() = setOf(STRING, LIST, COMPOUND)

    val withIdentifier: Set<Pair<Type, Byte>>
        get() = Type.values().associateWith<Type, Byte> { type ->
            when (type) {
                BYTE -> 1
                SHORT -> 2
                INT -> 3
                LONG -> 4
                FLOAT -> 5
                DOUBLE -> 6
                BYTE_ARRAY -> 7
                STRING -> 8
                LIST -> 9
                COMPOUND -> 10
                INT_ARRAY -> 11
                LONG_ARRAY -> 12
                else -> throw IllegalStateException("No known identifier for type: ${type.name}")
            }
        }.toList().toSet()

    val withName: Set<Pair<Type, String>>
        get() = Type.values().associateWith { type ->
            when (type) {
                BYTE -> "Byte"
                SHORT -> "Short"
                INT -> "Int"
                LONG -> "Long"
                FLOAT -> "Float"
                DOUBLE -> "Double"
                BYTE_ARRAY -> "Byte_Array"
                STRING -> "String"
                LIST -> "List"
                COMPOUND -> "Compound"
                INT_ARRAY -> "Int_Array"
                LONG_ARRAY -> "Long_Array"
                else -> throw IllegalStateException("No known name for type: ${type.name}")
            }
        }.map { it.key to "TAG_${it.value}" }.toSet()

    fun allExcept(vararg excludes: Type) = all
        .filterNot { excludes.contains(it) }
        .toSet()

    fun compatibleWith(type: Type): Set<Type> = when (type) {
        BYTE, SHORT, INT, LONG, FLOAT, DOUBLE -> numeric
        BYTE_ARRAY, INT_ARRAY, LONG_ARRAY -> array
        else -> setOf(type)
    }

    fun incompatibleWith(type: Type): Set<Type> = allExcept(
        *compatibleWith(type).toTypedArray()
    )
}