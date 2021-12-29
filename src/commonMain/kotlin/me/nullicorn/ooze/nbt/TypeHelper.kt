package me.nullicorn.ooze.nbt

/*
 * The "multiplatform" dev build that adds JS enum support has some trouble with internal properties
 * on enums. Until that's fixed, this extension property is the workaround.
 *
 * In this case "trouble" means that all code accessing the property references the non-obfuscated
 * name ("runtimeType"), but the property itself compiles to an obfuscated name
 * ("_get_runtimeType__3579803807"). So when something tries to access it, it uses the wrong name
 * and just gets `undefined`.
 *
 * As soon as possible, this should be made a constructor of [Type].
 *
 * Pending issue [KT-50504]: https://youtrack.jetbrains.com/issue/KT-50504
 */

/**
 * The class to be used by values of the current type.
 */
internal val Type.runtimeType
    get() = when (this) {
        Type.BYTE -> Byte::class
        Type.SHORT -> Short::class
        Type.INT -> Int::class
        Type.LONG -> Long::class
        Type.FLOAT -> Float::class
        Type.DOUBLE -> Double::class
        Type.BYTE_ARRAY -> ByteArray::class
        Type.INT_ARRAY -> IntArray::class
        Type.LONG_ARRAY -> LongArray::class
        Type.STRING -> String::class
        Type.LIST -> TagList::class
        Type.COMPOUND -> TagCompound::class
    }