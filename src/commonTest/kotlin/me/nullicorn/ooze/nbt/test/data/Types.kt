package me.nullicorn.ooze.nbt.test.data

import me.nullicorn.ooze.nbt.Type
import me.nullicorn.ooze.nbt.Type.*
import me.nullicorn.ooze.nbt.test.convert
import me.nullicorn.ooze.nbt.test.data.Types.all

/**
 * Data for testing the [Type] enum, as well as features that use it.
 */
object Types {
    /**
     * All NBT types implemented by the library.
     *
     * @see[Type]
     */
    val all: Set<Type>
        get() = Type.values().toSet()

    /**
     * All NBT types implemented by the library, mapped to their numeric (8-bit) identifier.
     *
     * @see[all]
     */
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

    /**
     * All NBT types implemented by the library, mapped to their standard UTF-8 names.
     *
     * @see[all]
     */
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

    /**
     * @param[excludes] Any NBT types that will **not** be supplied. When empty, this function
     * behaves the same as [all].
     * @return all NBT types that are not provided as arguments.
     *
     * @see[all]
     */
    fun allExcept(vararg excludes: Type) = all
        .filterNot { excludes.contains(it) }
        .toSet()

    /**
     * All NBT types that represent individual numbers.
     */
    private val numeric = setOf(BYTE, SHORT, INT, LONG, FLOAT, DOUBLE)

    /**
     * All NBT types that represent ordered sequences of numbers.
     */
    private val array = setOf(BYTE_ARRAY, INT_ARRAY, LONG_ARRAY)

    /**
     * @return any types whose values should be implicitly converted into the supplied [type] by the
     * library. This set will always include the provided [type].
     *
     * @see[convert]
     */
    fun compatibleWith(type: Type): Set<Type> = when {
        numeric.contains(type) -> numeric
        array.contains(type) -> array
        else -> setOf(type)
    }

    /**
     * @return any types whose values should **not** be implicitly converted into the supplied
     * [type] by the library. This set will never include the provided [type].
     *
     * @see[convert]
     */
    fun incompatibleWith(type: Type): Set<Type> = allExcept(
        *compatibleWith(type).toTypedArray()
    )
}