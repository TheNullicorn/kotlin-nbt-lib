package me.nullicorn.ooze.nbt

/**
 * An unordered set of NBT tags, each with a unique name within the current scope (the compound
 * itself).
 */
class TagCompound internal constructor(private val entries: MutableSet<Entry>) : Iterable<Entry> {

    /**
     * The number of entries in the compound.
     */
    val size get() = entries.size

    /**
     * Indicates that the compound has no entries.
     *
     * @return `true` if [size]` == 0`. Otherwise `false`.
     */
    fun isEmpty() = entries.isEmpty()

    /**
     * The names of each of the compound's direct child entries.
     *
     * This does not include the names of entries in nested compounds–compounds within the current
     * one; only direct child tag names.
     */
    val names: Iterable<String> get() = entries.map(Entry::name).toSet()

    /**
     * Checks if the compound contains any entry for a specific [name].
     *
     * @param[name] The entry's unique identifier within the compound.
     * @return `true` if the compound directly has an entry using the [name] supplied. Otherwise
     * `false`.
     */
    fun contains(name: String) = entries.any { it.name == name }

    /**
     * Checks if the compound contains any entry for a specific [name] that also uses a specific
     * [type].
     *
     * @param[name] The entry's unique identifier within the compound.
     * @param[type] The NBT type that the entry should use.
     * @return `true` if the compound directly has an entry using the [name] and [type] supplied.
     * Otherwise `false`.
     */
    fun contains(name: String, type: Type) = entries.any { it.name == name && it.type == type }

    /**
     * Retrieves the NBT type of the entry with a specific [name].
     *
     * @param[name] The entry's unique identifier within the compound.
     * @return the entry's NBT type, or `null` if the compound has no entry with the [name]
     * supplied.
     * @see[get]
     */
    fun typeOf(name: String) = entries.firstOrNull { it.name == name }?.type

    /**
     * Retrieves an entry in the compound with a specific [name].
     *
     * @param[name] The entry's unique identifier within the compound.
     * @return the entry's [value][Entry.value], or `null` if the compound has no entry with the
     * [name] supplied.
     */
    operator fun get(name: String) = entries.firstOrNull {
        it.name == name
    }?.value

    /**
     * Retrieves an entry in the compound with a specific [name], only if its [type][Entry.type] is
     * equal to the one specified.
     *
     * Since the type is known ahead of time, it is recommended to use one of the type-specific
     * getters this class offers, which take care of type checking and casting automatically.
     * - [getByte] for [bytes][Type.BYTE]
     * - [getShort] for [shorts][Type.SHORT]
     * - [getInt] for [ints][Type.INT]
     * - [getLong] for [longs][Type.LONG]
     * - [getFloat] for [floats][Type.FLOAT]
     * - [getDouble] for [doubles][Type.DOUBLE]
     * - [getByteArray] for [byte arrays][Type.BYTE_ARRAY]
     * - [getIntArray] for [int arrays][Type.INT_ARRAY]
     * - [getLongArray] for [long arrays][Type.LONG_ARRAY]
     * - [getString] for [strings][Type.STRING]
     * - [getList] for [lists][Type.LIST]
     * - [getCompound] for inner [compounds][Type.COMPOUND]
     *
     * @param[name] The entry's unique identifier within the compound.
     * @param[type] The expected [Type] for the entry to have.
     * @return the entry's [value][Entry.value], or `null` if the compound has no entry with the
     * [name] *and* [type] supplied.
     * @see[get]
     */
    operator fun get(name: String, type: Type) = entries.firstOrNull {
        it.name == name && it.type == type
    }?.value

    /**
     * Assigns a [value] to a tag under a specific [name] in the compound.
     *
     * Any entry already using the [name] will be replaced by the new and [value].
     *
     * @param[name] The entry's unique identifier within the compound.
     * @param[type] The NBT [Type] that will be used to represent the value.
     * @param[value] The contents of the tag.
     */
    operator fun set(name: String, type: Type, value: Any) {
        // Get rid of any entry already using that name.
        entries.removeAll { it.name == name }
        // Insert the new entry.
        entries.add(Entry(type, name, value))
    }

    /**
     * Removes any entry from the array identified by a specific [name].
     *
     * @param[name] The entry's unique identifier within the compound.
     * @return `true` if the entry was removed. `false` if the compound had no tag with the [name]
     * supplied
     */
    fun remove(name: String) = entries.removeAll { it.name == name }

    override fun iterator() = entries.iterator()

    ///////////////////////////////////////////////////////////////////////////
    // Type-Specific Getters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Retrieves the value of a byte tag with a specific [name].
     *
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.BYTE) as? Byte
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getByte(name: String) = get(name, Type.BYTE) as? Byte

    /**
     * Retrieves the value of a short tag with a specific [name].
     *
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.SHORT) as? Short
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getShort(name: String) = get(name, Type.SHORT) as? Short

    /**
     * Retrieves the value of an int tag with a specific [name].
     *
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.INT) as? Int
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getInt(name: String) = get(name, Type.INT) as? Int

    /**
     * Retrieves the value of a long tag with a specific [name].
     *
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.LONG) as? Long
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getLong(name: String) = get(name, Type.LONG) as? Long

    /**
     * Retrieves the value of a float tag with a specific [name].
     *
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.FLOAT) as? Float
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getFloat(name: String) = get(name, Type.FLOAT) as? Float

    /**
     * Retrieves the value of a double tag with a specific [name].
     *
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.DOUBLE) as? Double
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getDouble(name: String) = get(name, Type.DOUBLE) as? Double

    /**
     * Retrieves the value of a byte array tag with a specific [name].
     *
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.BYTE_ARRAY) as? ByteArray
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getByteArray(name: String) = get(name, Type.BYTE_ARRAY) as? ByteArray

    /**
     * Retrieves the value of an int array tag with a specific [name].
     *
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.INT_ARRAY) as? IntArray
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getIntArray(name: String) = get(name, Type.INT_ARRAY) as? IntArray

    /**
     * Retrieves the value of a long array tag with a specific [name].
     *
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.LONG_ARRAY) as? LongArray
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getLongArray(name: String) = get(name, Type.LONG_ARRAY) as? LongArray

    /**
     * Retrieves the value of a string tag with a specific [name].
     *
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.STRING) as? String
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getString(name: String) = get(name, Type.STRING) as? String

    /**
     * Retrieves the value of a compound tag with a specific [name], inside the current compound.
     *
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.COMPOUND) as? TagCompound
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getCompound(name: String) = get(name, Type.COMPOUND) as? TagCompound

    /**
     * Retrieves the value of a list tag with a specific [name].
     *
     * If a non-null [contentType] is provided, then a match will only be returned if the
     * [names][name] matches, the entry is a [list][Type.LIST], *and* the
     * [contentTypes][TagList.contentType] match.
     *
     * If [contentType] is not provided or is `null`, the only criteria is that the [names][name]
     * match and the match is a [list][Type.LIST]. The returned list may have any
     * [contentType][TagList.contentType].
     *
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getList(name: String, contentType: Type? = null) = entries.firstOrNull {
        it.name == name
                && it.type == Type.LIST
                && it.value is TagList
                && it.value.contentType == contentType
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as TagCompound

        // The Entry class has its equals(), which is implicitly used here.
        if (entries != other.entries) return false

        return true
    }

    override fun hashCode(): Int {
        // The Entry class has its own hashCode(), which is implicitly used here.
        return entries.hashCode()
    }
}