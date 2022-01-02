@file:Suppress("unused")

package me.nullicorn.ooze.nbt

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import me.nullicorn.ooze.nbt.testutil.data.Types

class TypeTests : ShouldSpec({
    context("Type.identifier") {
        should("return the type's identifying byte") {
            withData(Types.withIdentifier) { (type, identifier) ->
                type.identifier shouldBe identifier
            }
        }
    }

    context("Type.fromIdentifier") {
        should("return the type associated with the identifier") {
            withData(Types.withIdentifier) { (type, identifier) ->
                Type.fromIdentifier(identifier) shouldBe type
            }
        }
    }

    context("Type.toString") {
        should("return the standard name for the NBT type") {
            withData(Types.withName) { (type, name) ->
                type.toString() shouldBe name
            }
        }
    }
})