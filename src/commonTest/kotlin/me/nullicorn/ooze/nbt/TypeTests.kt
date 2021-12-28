package me.nullicorn.ooze.nbt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class TypeTests {

    @Test
    fun ensure_types_have_correct_identifiers() {
        for (type in Type.values()) {
            val expected = when (type) {
                Type.BYTE -> 1
                Type.SHORT -> 2
                Type.INT -> 3
                Type.LONG -> 4
                Type.FLOAT -> 5
                Type.DOUBLE -> 6
                Type.BYTE_ARRAY -> 7
                Type.STRING -> 8
                Type.LIST -> 9
                Type.COMPOUND -> 10
                Type.INT_ARRAY -> 11
                Type.LONG_ARRAY -> 12
                else -> throw IllegalStateException("No known ID for type: $type")
            }.toByte()

            assertEquals(expected, type.identifier)
        }
    }

    @Test
    fun ensure_lookup_returns_correct_type_for_identifier() {
        for (id in 1..12) {
            val expected = when (id) {
                1 -> Type.BYTE
                2 -> Type.SHORT
                3 -> Type.INT
                4 -> Type.LONG
                5 -> Type.FLOAT
                6 -> Type.DOUBLE
                7 -> Type.BYTE_ARRAY
                8 -> Type.STRING
                9 -> Type.LIST
                10 -> Type.COMPOUND
                11 -> Type.INT_ARRAY
                12 -> Type.LONG_ARRAY
                else -> throw IllegalStateException("No known type for ID: $id")
            }

            assertEquals(expected, Type.fromIdentifier(id.toByte()))
        }
    }

    @Test
    fun ensure_lookup_returns_null_for_any_other_value() {
        for (id in Byte.MIN_VALUE..Byte.MAX_VALUE) {
            // Skip valid identifiers.
            if (id in 1..12) continue

            assertNull(Type.fromIdentifier(id.toByte()))
        }
    }

    @Test
    fun ensure_toString_returns_official_name() {
        for (type in Type.values()) {
            val expected = when (type) {
                Type.BYTE -> "Byte"
                Type.SHORT -> "Short"
                Type.INT -> "Int"
                Type.LONG -> "Long"
                Type.FLOAT -> "Float"
                Type.DOUBLE -> "Double"
                Type.BYTE_ARRAY -> "Byte_Array"
                Type.STRING -> "String"
                Type.LIST -> "List"
                Type.COMPOUND -> "Compound"
                Type.INT_ARRAY -> "Int_Array"
                Type.LONG_ARRAY -> "Long_Array"
                else -> throw IllegalStateException("No known name for type: $type")
            }

            assertEquals("TAG_$expected", type.toString())
        }
    }
}