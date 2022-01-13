package me.nullicorn.ooze.nbt.test

import me.nullicorn.ooze.nbt.test.data.stringValues

/**
 * Data for testing the limits of NBT names: the UTF-8 strings used to differentiate between tags in
 * a compound.
 */
object Names {

    /**
     * Some limit-testing names, including empty strings, full-length strings, strings with
     * non-ASCII characters, etc.
     */
    private val all: Set<String> get() = stringValues.toSet()

    /**
     * A sample name that can be used with tags created for tests that are meant to throw
     * exceptions.
     *
     * ```"IShouldBeBad"```
     */
    const val shouldThrow = "IShouldBeBad"

    /**
     * A sample name that can be used with tags created for tests that are not meant to throw
     * exceptions.
     *
     * ```"IShouldBeOkay"```
     */
    const val shouldntThrow = "IShouldBeOkay"

    fun forEach(test: (name: String) -> Unit) = all.forEach(test)
}