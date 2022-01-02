package me.nullicorn.ooze.nbt

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.spec.style.scopes.addTest
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import me.nullicorn.ooze.nbt.testutil.data.Entries
import me.nullicorn.ooze.nbt.testutil.data.Names
import me.nullicorn.ooze.nbt.testutil.data.Types
import me.nullicorn.ooze.nbt.testutil.data.Values
import me.nullicorn.ooze.nbt.testutil.withCompatibleValues
import me.nullicorn.ooze.nbt.testutil.withIncompatibleValues
import me.nullicorn.ooze.nbt.testutil.withTypes

class EntryTests : ShouldSpec({
    // Tests for the primary constructor.
    context("Entry()") {
        withTypes("type") { type ->
            withCompatibleValues(type) { compatibleValue, _ ->
                shouldNotThrow<IllegalArgumentException> {
                    Entry(type, Names.shouldntThrow, compatibleValue)
                }
            }

            withIncompatibleValues(type) { incompatibleValue ->
                shouldThrow<IllegalArgumentException> {
                    Entry(type, Names.shouldThrow, incompatibleValue)
                }
            }
        }
    }

    context("Entry.type") {
        should("be equal to the type provided in the constructor") {
            withData(Types.all) { type ->
                val entry = Entry(type, Names.shouldntThrow, Values.oneOf(type))

                entry.type shouldBe type
                entry.component2() shouldBe type
            }
        }
    }

    context("Entry.name") {
        should("be equal to the name provided in the constructor") {
            withData<String>(Names.testNamer, Names.all) { name ->
                val entry = Entry(Type.BYTE, name, 0)

                entry.name shouldBe name
                entry.component1() shouldBe name
            }
        }
    }

    context("Entry.value") {
        context("should be equal to the value provided in the constructor") {
            withTypes("type") { type ->
                withCompatibleValues(type) { value, convertedValue ->
                    val entry = Entry(type, Names.shouldntThrow, value)

                    entry.value shouldBe convertedValue
                    entry.component3() shouldBe convertedValue
                }
            }
        }
    }

    context("Entry.as[Type]") {
        withTypes("type") { type ->
            val entry = Entry(type, Names.shouldntThrow, Values.oneOf(type))

            should("return the entry's value when the $type getter is used") {
                Entries.getterFor(type).get(entry) shouldBe entry.value
            }

            for (wrongType in Types.allExcept(type)) {
                should("throw when the $wrongType getter is used") {
                    shouldThrow<IllegalStateException> {
                        Entries.getterFor(wrongType).get(entry)
                    }
                }
            }
        }
    }
})