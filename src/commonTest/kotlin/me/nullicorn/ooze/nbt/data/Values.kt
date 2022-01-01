package me.nullicorn.ooze.nbt.data

import me.nullicorn.ooze.nbt.Type
import me.nullicorn.ooze.nbt.Type.*
import me.nullicorn.ooze.nbt.data.values.*

object Values {

    fun forType(type: Type) = forTypes(type)

    fun forAllTypes() = forTypes(*Type.values())

    fun forNumericTypes() = forTypes(*Types.numeric.toTypedArray())

    fun forNonNumericTypes() = forTypes(*Types.nonNumeric.toTypedArray())

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

    fun oneOf(type: Type): Any = forType(type)
        .first { it.first == type }
        .second

    fun oneOfEach(vararg types: Type): Set<Pair<Type, Any>> = forTypes(*types)
        .distinctBy { it.first }
        .filter { types.contains(it.first) }
        .toSet()

    val oneOfEvery: Set<Pair<Type, Any>>
        get() = forTypes(*Type.values())
            .distinctBy { it.first }
            .toSet()
}