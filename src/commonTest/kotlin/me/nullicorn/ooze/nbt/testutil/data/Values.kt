package me.nullicorn.ooze.nbt.testutil.data

import me.nullicorn.ooze.nbt.TagCompound
import me.nullicorn.ooze.nbt.TagList
import me.nullicorn.ooze.nbt.Type
import me.nullicorn.ooze.nbt.Type.*
import me.nullicorn.ooze.nbt.testutil.data.values.*
import me.nullicorn.ooze.nbt.testutil.data.values.byteArrayValues
import me.nullicorn.ooze.nbt.testutil.data.values.byteValues
import me.nullicorn.ooze.nbt.testutil.data.values.compoundValues
import me.nullicorn.ooze.nbt.testutil.data.values.doubleValues
import me.nullicorn.ooze.nbt.testutil.data.values.floatValues

object Values {

    fun forType(type: Type) = forTypes(type).map { it.second }.toSet()

    fun forAllTypes() = forTypes(*values())

    fun forAllTypesExcept(vararg excluded: Type) = forTypes(*values()
        .toMutableSet()
        .apply { removeAll(excluded.toSet()) }
        .toTypedArray())

    fun forNumericTypes() = forTypes(Types.numeric)

    fun forNonNumericTypes() = forTypes(Types.nonNumeric)

    fun forArrayTypes() = forTypes(Types.array)

    fun forNonArrayTypes() = forTypes(Types.nonArray)

    fun forTypes(types: Iterable<Type>) = forTypes(*types.toSet().toTypedArray())

    fun forTypes(vararg types: Type): Set<Pair<Type, Any>> = buildSet {
        // For each included type...
        for (type in Type.values()) if (types.contains(type)) {

            // ...get all of our stress-test values for that type...
            val valuesForType = when (type) {
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
            }.map { type to it } // ...and pair each value with the current type...

            /// ...and add those pairs to the map.
            addAll(valuesForType)
        }
    }.toList().toSet()

    fun oneOf(type: Type): Any = forType(type).first()

    fun oneOfEach(vararg types: Type): Set<Pair<Type, Any>> = forTypes(*types)
        .distinctBy { it.first }
        .filter { types.contains(it.first) }
        .toSet()

    val oneOfEvery: Set<Pair<Type, Any>>
        get() = forTypes(*values())
            .distinctBy { it.first }
            .toSet()
}