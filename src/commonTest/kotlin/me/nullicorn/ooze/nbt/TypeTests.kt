package me.nullicorn.ooze.nbt

import io.kotest.core.spec.style.WordSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class TypeTests : WordSpec({
    withData(
        Type.BYTE identifiedBy 1,
        Type.SHORT identifiedBy 2,
        Type.INT identifiedBy 3,
        Type.LONG identifiedBy 4,
        Type.FLOAT identifiedBy 5,
        Type.DOUBLE identifiedBy 6,
        Type.BYTE_ARRAY identifiedBy 7,
        Type.STRING identifiedBy 8,
        Type.LIST identifiedBy 9,
        Type.COMPOUND identifiedBy 10,
        Type.INT_ARRAY identifiedBy 11,
        Type.LONG_ARRAY identifiedBy 12,
    ) { (type, identifier) ->
        "Type.identifier" should {
            "return the type's identifying byte" {
                type.identifier shouldBe identifier
            }
        }

        "Type.fromIdentifier()" should {
            "return the type associated with the identifier" {
                Type.fromIdentifier(identifier) shouldBe type
            }
        }
    }
})

private data class IdentifiedType(val type: Type, val identifier: Byte)

private infix fun Type.identifiedBy(identifier: Byte) = IdentifiedType(this, identifier)