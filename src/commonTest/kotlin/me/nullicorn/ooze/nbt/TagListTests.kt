package me.nullicorn.ooze.nbt

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import me.nullicorn.ooze.nbt.testutil.data.Types
import me.nullicorn.ooze.nbt.testutil.withCompatibleValues
import me.nullicorn.ooze.nbt.testutil.withIncompatibleValues
import me.nullicorn.ooze.nbt.testutil.withTypes

class TagListTests : ShouldSpec({
    context("TagList.contentType") {
        should("be equal to the contentType provided in the constructor") {
            withData(Types.all) { contentType ->
                TagList(contentType).contentType shouldBe contentType
            }
        }
    }

    context("TagList.add()") {
        withTypes("contentType") { contentType ->
            val list = TagList(contentType)
            var size = 0

            should("start out empty") {
                list sizeShouldBe 0
            }

            withCompatibleValues(contentType) { compatibleValue, _ ->
                shouldNotThrow<IllegalArgumentException> {
                    list.add(compatibleValue)
                }

                // Make sure size increased by 1.
                list sizeShouldBe ++size
            }

            withIncompatibleValues(contentType) { incompatibleValue ->
                shouldThrow<IllegalArgumentException> {
                    list.add(incompatibleValue)
                }

                // Make sure size didn't change.
                list sizeShouldBe size
            }
        }
    }
})

infix fun TagList.sizeShouldBe(size: Int) {
    size shouldBe size
    lastIndex shouldBe size - 1
    isEmpty() shouldBe (size <= 0)
}