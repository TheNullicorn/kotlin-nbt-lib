package me.nullicorn.ooze.nbt.test.data

import io.kotest.datatest.withData
import me.nullicorn.ooze.nbt.test.data.values.stringValues

/**
 * Data for testing the limits of NBT names: the UTF-8 strings used to differentiate between tags in
 * a compound.
 */
object Names {

    /**
     * Some limit-testing names, including empty strings, full-length strings, strings with
     * non-ASCII characters, etc.
     */
    val all: Set<String> get() = stringValues.toSet()

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

    /**
     * A function for naming tests that use [Names.all] via Kotest's [withData] function.
     *
     * Kotest names tests based on the [toString] value of each object provided via
     * [withData][withData], but doesn't allow test names to be empty. [Names.all] may return empty
     * strings, so to prevent Kotest from complaining we wrap all strings in quotes and parentheses.
     */
    val testNamer: (String) -> String
        get() = { name: String -> "(\"$name\")" }
}