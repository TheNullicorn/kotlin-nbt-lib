package me.nullicorn.ooze.nbt.test.data.values

// Every 3rd byte, which includes Byte.MIN_VALUE and Byte.MAX_VALUE without needing all 255.
internal val byteValues: Array<Byte>
    get() = (Byte.MIN_VALUE..Byte.MAX_VALUE step 3)
        .map(Int::toByte)
        .toTypedArray()