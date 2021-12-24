package me.nullicorn.ooze.nbt.io

/**
 * Indicates that an operation depending on a data source failed unexpectedly.
 */
class InputException(
    override val message: String?,
    override val cause: Throwable? = null,
) : Exception()

/**
 * Executes code that might throw an [InputException], and rethrows a more detailed exception if one
 * occurs.
 *
 * The [action] describes what the [runnable] does, and is only used in the exception message, if
 * thrown:
 * ```kotlin
 * "Stream ended unexpectedly while $action"
 * ```
 *
 * @param[action] A description of the [runnable]'s task. Should use a present participle ("-ing").
 * @param[runnable] The code to execute, which may throw an [InputException].
 * @return the return value of the [runnable].
 *
 * @throws[InputException] if the [runnable] also throws an [InputException].
 */
internal fun <T> runUnsafeInput(action: String, runnable: () -> T): T = try {
    runnable()
} catch (cause: InputException) {
    throw InputException("Stream ended unexpectedly while $action", cause)
}