package me.nullicorn.ooze.nbt

import me.nullicorn.ooze.nbt.test.Types
import me.nullicorn.ooze.nbt.test.shouldEqual
import kotlin.js.JsName
import kotlin.test.Test

class TypeTests {
    @Test
    @JsName("identifierTest")
    fun `identifier should return the type's identifying byte`() {
        for ((type, identifier) in Types.withIdentifier) {
            type.identifier shouldEqual identifier
        }
    }

    @Test
    @JsName("fromIdentifierTest")
    fun `fromIdentifier() should return the type associated with the identifier`() {
        for ((type, identifier) in Types.withIdentifier) {
            Type.fromIdentifier(identifier) shouldEqual type
        }
    }

    @Test
    @JsName("toStringTest")
    fun `toString() should return the standard name for the type`() {
        for ((type, name) in Types.withName) {
            type.toString() shouldEqual name
        }
    }
}