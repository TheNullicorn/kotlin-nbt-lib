package me.nullicorn.ooze.nbt.io

import me.nullicorn.ooze.nbt.Entry
import me.nullicorn.ooze.nbt.TagCompound
import me.nullicorn.ooze.nbt.TagList
import me.nullicorn.ooze.nbt.Type
import me.nullicorn.ooze.nbt.io.codec.*
import me.nullicorn.ooze.nbt.io.source.ByteArrayInputSource
import me.nullicorn.ooze.nbt.io.source.InputSource
import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
class NbtInput internal constructor(private val source: InputSource) {

    @JsName("ofBytes")
    constructor(bytes: ByteArray) : this(ByteArrayInputSource(bytes))

    fun readRootCompound(): TagCompound {
        val rootType = runUnsafeInput("reading root value's type") { readType() }

        return if (rootType == null) {
            // If the type is null (indicating TAG_End), then the root is considered empty.
            // Return a compound with no entries.
            TagCompound(mutableSetOf())
        } else if (rootType == Type.COMPOUND) {
            // Skip the root compound's name, which is usually empty anyway.
            readString()
            // Return the root compound.
            readCompound()
        } else {
            // The root value must be a compound; throw if it isn't.
            throw MalformedNbtException("Expected a ${Type.COMPOUND} as root, not $rootType")
        }
    }

    /**
     * Reads the next NBT [Type] identifier from the input source.
     *
     * If the identifier is `0`, `null` is returned. This is because `0` represents the NBT "End"
     * type, which is primarily used as a marker, not an actual type.
     *
     * If the identifier is not associated with any type, and is not `0` either, a
     * [MalformedNbtException] is thrown.
     *
     * @return the NBT [Type] associated with the identifier.
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     * @throws[MalformedNbtException] if the identifier provided by the source has no known [Type].
     */
    @Throws(InputException::class, MalformedNbtException::class)
    fun readType(): Type? {
        val contentTypeId = runUnsafeInput("reading type identifier") { source.readByte() }

        if (contentTypeId == NULL_TAG_ID)
            return null

        return Type.fromIdentifier(contentTypeId)
            ?: throw MalformedNbtException("Unknown NBT identifier: $contentTypeId")
    }

    /**
     * Reads the next byte from the input source.
     *
     * "Byte" refers to an `8`-bit, signed integer.
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     */
    @Throws(InputException::class)
    fun readByte() = source.readByte()

    /**
     * Reads the next short from the input source.
     *
     * "Short" refers to a `16`-bit, signed integer.
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     */
    @Throws(InputException::class)
    fun readShort() = source.readShort()

    /**
     * Reads the next int from the input source.
     *
     * "Int" refers to a `32`-bit, signed integer.
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     */
    @Throws(InputException::class)
    fun readInt() = source.readInt()

    /**
     * Reads the next long from the input source.
     *
     * "Long" refers to a `64`-bit, signed integer.
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     */
    @Throws(InputException::class)
    fun readLong() = source.readLong()

    /**
     * Reads the next float from the input source.
     *
     * "Float" refers to a single precision (`32`-bit), signed, floating point number.
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     */
    @Throws(InputException::class)
    fun readFloat() = source.readFloat()

    /**
     * Reads the next double from the input source.
     *
     * "Double" refers to a double precision (`64`-bit), signed, floating point number.
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     */
    @Throws(InputException::class)
    fun readDouble() = source.readDouble()

    /**
     * Reads a sequence of bytes from the input source, the length of which is also provided by the
     * source.
     *
     * "Byte" refers to an `8`-bit, signed integer.
     *
     * @return the array provided by the source.
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     * @throws[MalformedNbtException] if the source provides a negative length for the array.
     */
    @Throws(InputException::class, MalformedNbtException::class)
    fun readByteArray(): ByteArray {
        val length = readLength(Type.BYTE_ARRAY)
        return runUnsafeInput("reading byte array contents") { source.readToNewBuffer(length) }
    }

    /**
     * Reads a sequence of ints from the input, the length of which is also provided by the input.
     *
     * "Int" refers to a `32`-bit, signed integer.
     *
     * @return the array provided by the source.
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     * @throws[MalformedNbtException] if the source provides a negative length for the array.
     */
    @Throws(InputException::class, MalformedNbtException::class)
    fun readIntArray(): IntArray {
        val length = readLength(Type.INT_ARRAY)
        return runUnsafeInput("reading int array contents") { source.readIntArray(length) }
    }

    /**
     * Reads a sequence of longs from the input, the length of which is also provided by the input.
     *
     * "Long" refers to an `64`-bit, signed integer.
     *
     * @return the array provided by the source.
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     * @throws[MalformedNbtException] if the source provides a negative length for the array.
     */
    @Throws(InputException::class, MalformedNbtException::class)
    fun readLongArray(): LongArray {
        val length = readLength(Type.LONG_ARRAY)
        return runUnsafeInput("reading long array contents") { source.readLongArray(length) }
    }

    /**
     * Reads a sequence of UTF-8 characters from the input, and combines them as a string.
     *
     * @return the string provided by the source.
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     */
    @Throws(InputException::class)
    fun readString() = source.readString()

    /**
     * Reads a sequence of unnamed NBT tags from the input, all of which share the same [Type].
     *
     * @return the decoded tags provided by the source.
     * @see[TagList.contentType]
     *
     * @throws[InputException] if the source cannot be read from, or if it ends before the value can
     * be fully read.
     * @throws[MalformedNbtException] if the source provides a negative length for the list.
     * @throws[MalformedNbtException] if any of the list's elements are malformed.
     */
    @Throws(InputException::class, MalformedNbtException::class)
    fun readList(): TagList {
        val length = readLength(Type.LIST)

        val contentType = readType()
        if (contentType == null) {
            if (length > 0) throw MalformedNbtException("Non-empty lists must specify a type")
            // Fall-back to BYTE, the first type, since we don't keep an enum value for TAG_End.
            return TagList(Type.BYTE)
        }

        val elements = Array(length) {

        }

        return TagList(contentType, elements)
    }

    /**
     * Reads a set of named NBT tags from the input, which may each have their own [Type].
     */
    @Throws(InputException::class, MalformedNbtException::class)
    fun readCompound(): TagCompound {
        val entries = ArrayList<Entry>(16)

        while (true) {
            // Determine the next tag's type.
            val type = runUnsafeInput("reading tag type in compound") {
                readType()
            } ?: break // If we read null as a type (indicating TAG_End), then the compound is done.

            // Determine the next tag's name.
            val name = runUnsafeInput("reading tag name in compound (t=$type)") { readString() }

            // Read the value of the next tag.
            val value = runUnsafeInput("reading tag value in compound (t=$type, n=$name)") {
                readTag(type)
            }

            // Add the tag to the compound.
            entries.add(Entry(type, name, value))
        }

        return TagCompound(entries.toMutableSet())
    }

    /**
     * Reads the value of an NBT tag from the stream, given its [type].
     *
     * @param[type] The NBT type of the value to read.
     * @return the tag's value.
     *
     * @throws[InputException] if the tag or any of its child tags cannot be read from the input, or
     * if the input ends early.
     * @throws[MalformedNbtException] if the tag or any of its children do not represent valid NBT
     * data.
     */
    private fun readTag(type: Type): Any = when (type) {
        Type.BYTE -> readByte()
        Type.SHORT -> readShort()
        Type.INT -> readInt()
        Type.LONG -> readLong()
        Type.FLOAT -> readFloat()
        Type.DOUBLE -> readDouble()
        Type.BYTE_ARRAY -> readByteArray()
        Type.INT_ARRAY -> readIntArray()
        Type.LONG_ARRAY -> readLongArray()
        Type.STRING -> readString()
        Type.LIST -> readList()
        Type.COMPOUND -> readCompound()
    }

    /**
     * Reads the signed, 32-bit length prefix for an iterable tag.
     *
     * This functionality is shared by all 3 arrays, as well as tag lists.
     *
     * @param[type] The type of the tag that the length is for. This is only used to provide a more
     * detailed exception message, if thrown.
     * @return the encoded length value.
     *
     * @throws[InputException] if the input can't be read from, or if it ends early.
     * @throws[MalformedNbtException] if the length is a negative number.
     */
    private fun readLength(type: Type): Int {
        // Read the length value, which is an int.
        val length = runUnsafeInput("reading length of $type") { source.readInt() }

        // Make sure the length is non-negative.
        if (length < 0) {
            throw MalformedNbtException("$type cannot have a negative length: $length")
        }

        return length
    }

    private companion object {
        const val NULL_TAG_ID: Byte = 0
    }
}