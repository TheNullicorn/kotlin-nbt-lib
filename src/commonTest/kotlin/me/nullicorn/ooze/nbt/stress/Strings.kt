package me.nullicorn.ooze.nbt.stress

internal val stringValues: Array<String>
    get() = arrayOf(
        // Empty string.
        "",

        // A string of all printable ASCII codepoints.
        CharArray(95) { i -> (i + 32).toChar() }.concatToString(),

        // A string of all ASCII codepoints (including control chars).
        CharArray(128) { i -> i.toChar() }.concatToString(),

        // "Minecraft Formatting", but with a whole lotta color codes.
        "§0M§1i§2n§3e§4c§5r§6a§7f§8t §9F§ao§br§cm§da§et§ft§ki§ln§mg§r",

        // Dragging finger across the keyboard with Option key held.
        "`¡™£¢∞§¶•ªº–≠œ∑´®†¥¨ˆøπ“‘«åß∂ƒ©˙∆˚¬…æΩ≈ç√∫˜µ≤≥÷",

        // Dragging finger across the keyboard with Option+Shift keys held.
        "`⁄€‹›ﬁﬂ‡°·‚—±Œ„´‰ˇÁ¨ˆØ∏”’»ÅÍÎÏ˝ÓÔÒÚÆ¸˛Ç◊ı˜Â¯˘¿",

        // A max-length string (length = 2^16) of printable ASCII characters.
        CharArray(UShort.MAX_VALUE.toInt()) { i ->
            ((i * i * 255) % 95 + 32).toChar()
        }.concatToString(),
    )