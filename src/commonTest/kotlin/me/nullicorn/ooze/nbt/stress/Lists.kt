package me.nullicorn.ooze.nbt.stress

import me.nullicorn.ooze.nbt.TagCompound
import me.nullicorn.ooze.nbt.TagList
import me.nullicorn.ooze.nbt.Type

internal fun listValues(includeCompounds: Boolean = true) = buildList {
    // Empty lists, and lists of sample values; see TestValues.ofType().
    addAll(simpleLists)

    // A list of compounds (if enabled), some of which have more lists inside them.
    if (includeCompounds)
        add(listOfCompounds)

    // A list of other lists, including a list of compounds (if enabled).
    add(listOfLists.apply {
        add(listOfLists)

        if (includeCompounds)
            add(listOfCompounds)
    })
}.toTypedArray()

/**
 * Creates some "simple" lists of tags, meaning that none of the lists' elements are lists or compounds
 * themselves.
 *
 * This includes an empty list for each [Type], with the list's [contentType][TagList.contentType]
 * set to that type. This includes an empty list for [Type.LIST] and [Type.COMPOUND].
 *
 * This also includes a list containing the sample [values][TestValues.ofType] for each type,
 * excluding [Type.LIST] and [Type.COMPOUND].
 */
private val simpleLists: Array<TagList>
    get() = buildList {
        // Add an empty list with every possible contentType.
        for (type in Type.values()) add(TagList(type))

        // Add a list with each of our test/sample values for each type, besides lists & compounds.
        for (type in Type.values().filterNot { it == Type.LIST || it == Type.COMPOUND })
            add(TagList(type, TestValues.ofType<Any>(type)))
    }.toTypedArray()

/**
 * Creates a list of other list tags, which may contain other lists and compounds inside themselves.
 */
private val listOfLists: TagList
    get() = TagList(Type.LIST).apply {
        addAll(simpleLists)
    }

/**
 * Creates a list of compound tags, which may contain other lists and compounds within themselves.
 */
private val listOfCompounds: TagList
    get() = TagList(Type.COMPOUND).apply {
        // Add each compound from our test values.
        for (compound in compoundValues(includeLists = false)) add(compound)

        // Add a compound with a single entry, which is just another list of lists.
        add(TagCompound().apply {
            setList("InnerList", listOfLists)
        })
    }