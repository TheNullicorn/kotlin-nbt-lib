package me.nullicorn.ooze.nbt.io

/**
 * Indicates that NBT data violates the NBT specification, and is likely corrupted.
 */
class MalformedNbtException(
    override val message: String?,
    override val cause: Throwable? = null,
) : Exception()