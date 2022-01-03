package me.nullicorn.ooze.nbt.test.data.values

import me.nullicorn.ooze.nbt.Type

/**
 * Provides stress-testing values to be used with tags using the current [Type].
 */
@Suppress("UNCHECKED_CAST")
internal val Type.stressTestValues: Array<Any>
    get() = when (this) {
        Type.BYTE -> byteValues
        Type.SHORT -> shortValues
        Type.INT -> intValues
        Type.LONG -> longValues
        Type.FLOAT -> floatValues
        Type.DOUBLE -> doubleValues
        Type.BYTE_ARRAY -> byteArrayValues
        Type.INT_ARRAY -> intArrayValues
        Type.LONG_ARRAY -> longArrayValues
        Type.STRING -> stringValues
        Type.LIST -> listValues(includeCompounds = true)
        Type.COMPOUND -> compoundValues(includeLists = true)
        else -> throw IllegalArgumentException("No stress-test values for type: $this")
    } as Array<Any>