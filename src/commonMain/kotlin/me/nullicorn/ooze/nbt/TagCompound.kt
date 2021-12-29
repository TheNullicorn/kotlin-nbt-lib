package me.nullicorn.ooze.nbt

import kotlin.js.JsExport

/**
 * An unordered set of NBT tags, each with a unique name within the current scope (the compound
 * itself).
 */
@JsExport
class TagCompound(vararg entries: Entry) {

    private val entries = mutableSetOf(*entries)

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
    val names: Array<String> get() = entries.map(Entry::name).toTypedArray()

    /**
     * Checks if the compound contains any entry for a specific [name] that also uses a specific
     * [type].
     *
     * @param[name] The entry's unique identifier within the compound.
     * @param[type] The NBT type that the entry should use. If `null`, all types are considered.
     * @return `true` if the compound directly has an entry using the [name] and [type] supplied.
     * Otherwise `false`.
     */
    fun contains(name: String, type: Type? = null): Boolean =
        if (type == null) entries.any { it.name == name }
        else entries.any { it.name == name && it.type == type }

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
     * If the type is known ahead of time, it is recommended to use one of the type-specific
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
     * @param[type] The expected [Type] for the entry to have. If `null`, all types will be
     * considered.
     * @return the entry's [value][Entry.value], or `null` if the compound has no entry with the
     * [name] and (optionally) [type] supplied.
     */
    operator fun get(name: String, type: Type? = null) = entries.firstOrNull {
        it.name == name && (type == null || it.type == type)
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
     * This function is equivalent to
     * ```kotlin
     * get(name, Type.LIST) as? TagList
     * ```
     * The general contract of [get] also applies to this method.
     * @see[get]
     */
    fun getList(name: String) = get(name, Type.LIST) as? TagList

    /**
     * Retrieves the value of a list tag with a specific [name].
     *
     * A match will only be returned if the [names][name] matches, the entry is a [list][Type.LIST],
     * *and* the [contentTypes][TagList.contentType] match.
     *
     * If only [name] matters, use [getList].
     *
     * The general contract of [get] also applies to this method.
     * @see[get]
     * @see[getList]
     */
    fun getListOf(contentType: Type, name: String) = entries.firstOrNull {
        it.name == name
                && it.type == Type.LIST
                && it.value is TagList
                && it.value.contentType == contentType
    }?.value

    ///////////////////////////////////////////////////////////////////////////
    // Type-Specific Setters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Assigns the value of the tag with the specified [name] to a byte [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.BYTE, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setByte(name: String, value: Byte) = set(name, Type.BYTE, value)

    /**
     * Assigns the value of the tag with the specified [name] to a short [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.SHORT, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setShort(name: String, value: Short) = set(name, Type.SHORT, value)

    /**
     * Assigns the value of the tag with the specified [name] to an int [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.INT, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setInt(name: String, value: Int) = set(name, Type.INT, value)

    /**
     * Assigns the value of the tag with the specified [name] to a long [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.LONG, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setLong(name: String, value: LongArray) = set(name, Type.LONG, value)

    /**
     * Assigns the value of the tag with the specified [name] to a float [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.FLOAT, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setFloat(name: String, value: Float) = set(name, Type.FLOAT, value)

    /**
     * Assigns the value of the tag with the specified [name] to a double [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.DOUBLE, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setDouble(name: String, value: Double) = set(name, Type.DOUBLE, value)

    /**
     * Assigns the value of the tag with the specified [name] to an array of bytes, [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.BYTE_ARRAY, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setByteArray(name: String, value: ByteArray) = set(name, Type.BYTE_ARRAY, value)

    /**
     * Assigns the value of the tag with the specified [name] to an array of ints, [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.INT_ARRAY, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setIntArray(name: String, value: IntArray) = set(name, Type.INT_ARRAY, value)

    /**
     * Assigns the value of the tag with the specified [name] to an array of longs, [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.LONG_ARRAY, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setLongArray(name: String, value: LongArray) = set(name, Type.LONG_ARRAY, value)

    /**
     * Assigns the value of the tag with the specified [name] to a string [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.STRING, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setString(name: String, value: String) = set(name, Type.STRING, value)

    /**
     * Assigns the value of the tag with the specified [name] to a list of tags, [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.LIST, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setList(name: String, value: TagList) = set(name, Type.LIST, value)

    /**
     * Assigns the value of the tag with the specified [name] to an inner compound [value].
     *
     * This function is equivalent to
     * ```kotlin
     * set(name, Type.COMPOUND, value)
     * ```
     * The general contract of [set] also applies to this method.
     * @see[set]
     */
    fun setCompound(name: String, value: TagCompound) = set(name, Type.COMPOUND, value)

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