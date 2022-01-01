package me.nullicorn.ooze.nbt

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import me.nullicorn.ooze.nbt.data.Entries
import me.nullicorn.ooze.nbt.data.Names
import me.nullicorn.ooze.nbt.data.Types
import me.nullicorn.ooze.nbt.data.Values

class EntryTests : ShouldSpec({
    // Tests for the primary constructor.
    context("Entry()") {
        should("accept values with the correct class, given the type") {
            withData(Values.forAllTypes()) { (type, value) ->
                shouldNotThrow<IllegalArgumentException> {
                    Entry(type, Names.shouldntThrow, value)
                }
            }
        }

        should("accept any number, given a numeric type") {
            // Check for each numeric type.
            withData(Types.numeric) { numericType ->
                // Check against all numeric values we have.
                withData(Values.forNumericTypes()) { (_, numericValue) ->
                    shouldNotThrow<IllegalArgumentException> {
                        Entry(numericType, Names.shouldntThrow, numericValue)
                    }
                }
            }
        }

        // TODO: 12/31/21 Should accept any array, given an array type (TAG_Byte_Array, etc).

        should("throw if non-numeric values are used, given a numeric type") {
            // Check for each numeric type.
            withData(Types.numeric) { numericType ->
                // Check against all non-numeric values we have.
                withData(Values.forNonNumericTypes()) { (_, nonNumericValue) ->
                    shouldThrow<IllegalArgumentException> {
                        Entry(numericType, Names.shouldThrow, nonNumericValue)
                    }
                }
            }
        }

        should("throw throw if numeric values are used, given a non-numeric type") {
            // Check for each non-numeric type.
            withData(Types.nonNumeric) { nonNumericType ->
                // Check against all numeric values we have.
                withData(Values.forNumericTypes()) { (_, numericValue) ->
                    shouldThrow<IllegalArgumentException> {
                        Entry(nonNumericType, Names.shouldThrow, numericValue)
                    }
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
        should("be equal to the value provided in the constructor") {
            withData(Values.forAllTypes()) { (type, value) ->
                val entry = Entry(type, Names.shouldntThrow, value)

                entry.value shouldBe value
                entry.component3() shouldBe value
            }
        }
    }

    context("Entry.as[Type]") {
        withData(Types.all) { type ->
            val entry = Entry(type, Names.shouldntThrow, Values.oneOf(type))

            should("return the entry's value for the correct getter") {
                Entries.getterFor(type).get(entry) shouldBe entry.value
            }

            should("throw if the wrong getter is used") {
                withData(Types.allExcept(type)) { wrongType ->
                    shouldThrow<IllegalStateException> {
                        Entries.getterFor(wrongType).get(entry)
                    }
                }
            }
        }
    }
})