package me.nullicorn.ooze.nbt

import kotlin.js.JsExport

/**
 * An ordered collection of unnamed NBT tags, all with the same [Type].
 *
 * Duplicate elements are supported, meaning the same value and/or instance can appear in the list
 * multiple times at different indices, and can be retrieved from any index it exists at. Duplicate
 * values will also be serialized multiple times when written to an output destination as NBT.
 *
 * @param[contentType] The NBT type shared by all elements in the list.
 */
@JsExport
@Suppress("NON_EXPORTABLE_TYPE") // See comment on iterator().
class TagList(val contentType: Type, vararg elements: Any) : Iterable<Any> {

    private val elements: MutableList<Any> = checkValues(elements).toMutableList()

    /**
     * The number of elements in the list.
     */
    val size get() = elements.size

    /**
     * The index of the last element in the list.
     *
     * If the list is [empty][isEmpty], this will be `-1`.
     */
    val lastIndex get() = elements.lastIndex

    /**
     * Indicates that the list has no elements.
     *
     * @return `true` if [size]` == 0`. Otherwise `false`.
     */
    fun isEmpty() = elements.isEmpty()

    /**
     * Retrieves the element at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the element to retrieve.
     * @return the value of the element.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` (inclusive) and the
     * list's [size] (exclusive).
     */
    operator fun get(index: Int) = elements[index]

    /**
     * Replaces the element at a specific [index] in the list with a new value.
     *
     * @param[index] The zero-based list offset of the element to replace.
     * @param[value] The value to put at the [index].
     * @return the value that was previously at the index.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalArgumentException] if the [value] is not compatible with the list's
     * [contentType].
     */
    operator fun set(index: Int, value: Any) = elements.set(index, checkValue(value))

    /**
     * Appends a new [value] at the end of the list.
     *
     * This operation increases the list's [size] by `1`.
     *
     * @param[value] The value to insert at the end of the list.
     *
     * @throws[IllegalArgumentException] if the [value] is not compatible with the list's
     * [contentType].
     */
    fun add(value: Any) {
        val safeValue = checkValue(value)
        elements.add(safeValue)
    }

    /**
     * Inserts a new [value] into the list at a given [index].
     *
     * Any elements previously at or beyond the [index] will have their indices increased by `1`.
     * The indices of elements before the [index] are unaffected.
     *
     * This operation increases the list's [size] by `1`.
     *
     * @param[value] The value to insert at the [index].
     * @param[index] The index that the new [value] will be held at. This must be less than or equal
     * to the list's [size]. If not set, it defaults to the list's [size], making the value the last
     * element in the list.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's [size],
     * both inclusive.
     * @throws[IllegalArgumentException] if the [value] is not compatible with the list's
     * [contentType].
     */
    fun addAt(index: Int, value: Any) {
        val safeValue = checkValue(value)
        elements.add(index, safeValue)
    }

    /**
     * Adds any supplied [values] to the end of the list, in order.
     *
     * The first supplied value will be [added][add] at the supplied [lastIndex]` + 1`.
     * The next value will be added at [lastIndex]` + 2`. And so on.
     *
     * This operation increases the list's [size] by the number of [values] supplied.
     *
     * @param[values] The values to insert into the list.
     *
     * @throws[IllegalArgumentException] if any of the supplied [values] are not compatible with the
     * list's [contentType]. In that case, none of the values will be added, and the list will not
     * be modified.
     */
    fun addAll(vararg values: Any) {
        val safeValues = checkValues(values)
        elements.addAll(safeValues)
    }

    /**
     * Adds any supplied [values] to the list, in order, starting at a given [index].
     *
     * Any elements previously at or beyond the [index] will have their indices increased by the
     * number of [values] supplied. The indices of elements before the [index] are unaffected.
     *
     * For example, the first supplied value will be [added][add] at the supplied [index]. The next
     * value will be added at [index]` + 1`. And so on.
     *
     * This operation increases the list's [size] by the number of [values] supplied.
     *
     * @param[index] The index to start adding elements at. See above.
     * @param[values] The values to insert into the list.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's [size],
     * both inclusive.
     * @throws[IllegalArgumentException] if any of the supplied [values] are not compatible with the
     * list's [contentType]. In that case, none of the values will be added, and the list will not
     * be modified.
     */
    fun addAllAt(index: Int, vararg values: Any) {
        val safeValues = checkValues(values)
        elements.addAll(index, safeValues)
    }

    /**
     * Remove a single instance of a [value] from the list, if present.
     *
     * If the operation succeeds, the list's [size] will decrease by `1`, and any elements that had
     * a greater index than the removed element will have their indices decreased by `1`.
     *
     * @return `true` if an instance the value was removed. `false` if the value isn't in the list.
     */
    fun remove(value: Any) = elements.remove(value)

    /**
     * Removes the element at a specific [index] in the list.
     *
     * Any elements beyond the supplied [index] will have their indices decreased by `1`.
     *
     * @return the value of the removed element.
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     */
    fun removeAt(index: Int) = elements.removeAt(index)

    /**
     * Removes an instance of a value from the list for every instance of that value in a supplied
     * collection of [values].
     *
     * If a value appears multiple times in the list, it will only be removed however many times it
     * also appears in the supplied [values]. So if the list contains two instances of "Foo", and
     * the [values] has one instance, the list will still be left with a single "Foo" element.
     *
     * For each removed element, any elements with greater indices will have their indices decreased
     * by `1`.
     *
     * @return `true` if any elements were removed. Otherwise `false`.
     */
    fun removeAll(vararg values: Any): Boolean {
        val safeValues = checkValues(values)
        return elements.removeAll(safeValues)
    }

    /**
     * Checks if the list contains any elements [equal][Any.equals] to a specific [value].
     *
     * @return `true` if the list contains any instances of the [value]. Otherwise `false`.
     */
    fun contains(value: Any) = elements.contains(value)

    /**
     * Checks if the list contains at least one instance of each of the supplied [values].
     *
     * @return `true` if all supplied [values] have one or more equivalents in the list. Otherwise
     * `false`.
     */
    fun containsAll(vararg values: Any) = elements.containsAll(values.toSet())

    // Suppressed because iterator() doesn't get exported to JS anyway.
    // - This is intended for JVM users at the moment.
    // - JS users can still iterate using [size] and [get].
    @Suppress("NON_EXPORTABLE_TYPE")
    override fun iterator() = elements.iterator()

    ///////////////////////////////////////////////////////////////////////////
    // Internal Helper Methods.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Gets the value of the element at a specific index, assuming the value's [type].
     *
     * @return the value of the element at the [index], cast to [T].
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if [type] is not the same as the list's [contentType].
     */
    private inline fun <reified T> getWithType(index: Int, type: Type): T {
        check(type == contentType) {
            "Cannot get $type from a list of $contentType (i=$index)"
        }
        return get(index) as T
    }

    /**
     * Ensures that a [value] is the correct type to be inserted into the list, given the list's
     * [contentType].
     *
     * @return the original [value], allowing the function can be supplied as an argument.
     *
     * @throws[IllegalArgumentException] if the value's class does not match the one required by the
     * [contentType].
     */
    private inline fun <reified T : Any> checkValue(value: T): T = try {
        value asNbt contentType
    } catch (cause: IllegalStateException) {
        throw IllegalArgumentException(cause.message, cause)
    }

    /**
     * Ensures that a series of [values] are all the correct type to be inserted into the list,
     * given the list's [contentType].
     *
     * @return the collection containing of each of the acceptable values.
     *
     * @throws[IllegalArgumentException] if any of the values' classes do not match the one required
     * by the [contentType].
     */
    private fun <T : Any> checkValues(values: Array<T>): Collection<T> {
        // Retrieve all elements immediately, so that bad values can't be injected by modifying the
        // array after the check.
        val listOfValues = values.toList()

        // Make sure each value is valid, given the contentType.
        for (value in listOfValues) {
            checkValue<Any>(value)
        }

        // Return our extracted list, so that different (potentially bad) values can't be injected.
        // See above comment.
        return listOfValues
    }

    ///////////////////////////////////////////////////////////////////////////
    // Type-Specific Getters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Retrieves the byte at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the byte to retrieve.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.BYTE].
     */
    fun getByte(index: Int) = getWithType<Byte>(index, Type.BYTE)

    /**
     * Retrieves the short at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the short to retrieve.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.SHORT].
     */
    fun getShort(index: Int) = getWithType<Short>(index, Type.SHORT)

    /**
     * Retrieves the int at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the int to retrieve.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.INT].
     */
    fun getInt(index: Int) = getWithType<Int>(index, Type.INT)

    /**
     * Retrieves the long at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the long to retrieve.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.LONG].
     */
    @Suppress("NON_EXPORTABLE_TYPE")
    fun getLong(index: Int) = getWithType<Long>(index, Type.LONG)

    /**
     * Retrieves the float at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the float to retrieve.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.FLOAT].
     */
    fun getFloat(index: Int) = getWithType<Float>(index, Type.FLOAT)

    /**
     * Retrieves the double at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the double to retrieve.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.DOUBLE].
     */
    fun getDouble(index: Int) = getWithType<Double>(index, Type.DOUBLE)

    /**
     * Retrieves the byte array at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the array to retrieve.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.BYTE_ARRAY].
     */
    fun getByteArray(index: Int) = getWithType<ByteArray>(index, Type.BYTE_ARRAY)

    /**
     * Retrieves the int array at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the array to retrieve.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.INT_ARRAY].
     */
    fun getIntArray(index: Int) = getWithType<IntArray>(index, Type.INT_ARRAY)

    /**
     * Retrieves the long array at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the array to retrieve.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.LONG_ARRAY].
     */
    fun getLongArray(index: Int) = getWithType<LongArray>(index, Type.LONG_ARRAY)

    /**
     * Retrieves the string at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the string to retrieve.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.STRING].
     */
    fun getString(index: Int) = getWithType<String>(index, Type.STRING)

    /**
     * Retrieves the inner list at a specific [index] in the current list.
     *
     * @param[index] The zero-based offset of the inner list within the current one.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.LIST].
     */
    fun getList(index: Int) = getWithType<TagList>(index, Type.LIST)

    /**
     * Retrieves the compound at a specific [index] in the list.
     *
     * @param[index] The zero-based list offset of the compound to retrieve.
     *
     * @throws[IndexOutOfBoundsException] if the [index] is not between `0` and the list's
     * [lastIndex], or if the list is [empty][isEmpty].
     * @throws[IllegalStateException] if the list's [contentType] is not [Type.COMPOUND].
     */
    fun getCompound(index: Int) = getWithType<TagCompound>(index, Type.COMPOUND)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as TagList

        if (contentType != other.contentType) return false
        if (elements.size != other.elements.size) return false
        for ((i, element) in elements.withIndex()) {
            val otherElement = other.elements[i]
            if (element == otherElement) continue

            // Either the elements simply aren't equal, or they are arrays, and we need to use the
            // appropriate method.
            val areEqual = when (element) {
                is ByteArray -> otherElement is ByteArray && element.contentEquals(otherElement)
                is IntArray -> otherElement is IntArray && element.contentEquals(otherElement)
                is LongArray -> otherElement is LongArray && element.contentEquals(otherElement)
                // If they're not array's, then we know they're not equal from the earlier check.
                else -> false
            }

            if (!areEqual) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = contentType.hashCode()
        for (element in elements) {
            val elementHash = when (element) {
                // Hash arrays using their respective methods.
                is ByteArray -> element.contentHashCode()
                is IntArray -> element.contentHashCode()
                is LongArray -> element.contentHashCode()
                // Hash everything else normally.
                else -> element.hashCode()
            }
            // Combine the element's hash with the overall one.
            result = result * 31 + elementHash
        }
        return result
    }
}