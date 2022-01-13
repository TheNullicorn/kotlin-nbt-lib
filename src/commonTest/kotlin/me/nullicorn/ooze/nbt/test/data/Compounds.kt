package me.nullicorn.ooze.nbt.test.data

import me.nullicorn.ooze.nbt.Entry
import me.nullicorn.ooze.nbt.TagCompound
import me.nullicorn.ooze.nbt.Type

internal fun compoundValues(includeLists: Boolean = true) = arrayOf(
    // Test compound with no child tags.
    emptyCompound,

    // Test compound with empty, complicated, and long tag names.
    compoundWithComplexNames,

    // Test compound with only direct children (no inner lists or compounds).
    simpleCompound,

    // Test compound *with* inner lists and compounds, which themselves have children.
    simpleCompound.apply {
        val innerCompounds = arrayOf(emptyCompound, compoundWithComplexNames, simpleCompound)

        // Put the compounds inside the simple one, to test nesting.
        for ((index, compound) in innerCompounds.withIndex()) {
            // "TAG_Compound_0", etc
            setCompound(name = "${Type.COMPOUND}_$index", compound)
        }

        // Add the list test values to the compound (if enabled).
        if (includeLists) for ((index, list) in listValues(includeCompounds = false).withIndex()) {
            // "TAG_List_0", etc
            setList(name = "${Type.LIST}_$index", list)
        }
    }
)

/**
 * A compound with no [entries][Entry].
 */
private val emptyCompound
    get() = TagCompound()

/**
 * A compound with simple values, but names designed to test the limits of NBT strings.
 *
 * Names come from the [stringValues] array.
 *
 * All values are simply bytes set to `0`.
 */
private val compoundWithComplexNames
    get() = TagCompound().apply {
        for (name in stringValues) setByte(name, 0)
    }

/**
 * A compound with multiple values with each [Type], except for structural types ([Type.LIST] and
 * [Type.COMPOUND]).
 *
 * Values come from [Type.stressTestValues].
 */
private val simpleCompound
    get() = TagCompound().apply {
        for (type in Type.values()) {
            if (type == Type.LIST || type == Type.COMPOUND) continue

            for ((index, value) in type.stressTestValues.withIndex()) {
                // e.g. name = "TAG_Byte_0"
                set(name = type.toString() + "_" + index, type, value)
            }
        }
    }