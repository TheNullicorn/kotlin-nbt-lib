package me.nullicorn.ooze.nbt.stress

import me.nullicorn.ooze.nbt.Type

internal object TestValues {
    /**
     * Provides stress-testing values for a tag with the supplied [type].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> ofType(type: Type): Array<T> = when (type) {
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
        else -> throw IllegalArgumentException("No stress-test values for type: $type")
    } as Array<T>
}