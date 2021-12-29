package me.nullicorn.ooze.nbt.stress

internal val byteValues: Array<Byte>
    get() = (Byte.MIN_VALUE..Byte.MAX_VALUE).map(Int::toByte).toTypedArray()