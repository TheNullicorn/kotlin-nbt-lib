package me.nullicorn.ooze.nbt

import me.nullicorn.ooze.nbt.test.*
import kotlin.js.JsName
import kotlin.test.Test

class EntryTests {
    @Test
    @JsName("constructorTest_1")
    fun `Entry() should not throw for any value compatible with the supplied type`() =
        Types.forEach { targetType ->
            for (compatibleType in Types.compatibleWith(targetType))
                for (compatibleValue in Values.forType(compatibleType))
                    shouldNotThrow { Entry(targetType, Names.shouldntThrow, compatibleValue) }
        }

    @Test
    @JsName("constructorTest_2")
    fun `Entry() should throw for any value incompatible with the supplied type`() =
        Types.forEach { targetType ->
            for (incompatibleType in Types.incompatibleWith(targetType))
                for (incompatibleValue in Values.forType(incompatibleType))
                    shouldThrow<IllegalArgumentException> {
                        Entry(targetType, Names.shouldThrow, incompatibleValue)
                    }
        }

    @Test
    @JsName("typeTest")
    fun `type should be equal to the one supplied`() = Types.forEach { type ->
        val entry = Entry(type, Names.shouldntThrow, Values.oneOf(type))

        // Make sure the getter returns that type.
        entry.type shouldEqual type

        // Make sure the corresponding "component" function also returns that type.
        entry.component2() shouldEqual type
    }

    @Test
    @JsName("nameTest")
    fun `name should be equal to the one supplied`() = Types.forEach { type ->
        Names.forEach { name ->
            val entry = Entry(type, name, Values.oneOf(type))

            // Make sure the getter returns that name.
            entry.name shouldEqual name

            // Make sure the corresponding "component" function also returns that name.
            entry.component1() shouldEqual name
        }
    }

    @Test
    @JsName("genericGetterTest")
    fun `value should be equal or similar to the one supplied`() = Types.forEach { targetType ->
        Types.compatibleWith(targetType).forEach { compatibleType ->
            Values.forType(compatibleType).forEach { value ->

                val entry = Entry(targetType, Names.shouldntThrow, value)
                val expectedValue = convert(value) from compatibleType to targetType

                // Make sure the getter returns that value.
                entry.value shouldEqual expectedValue

                // Make sure the corresponding "component" function also returns that value.
                entry.component3() shouldEqual expectedValue
            }
        }
    }

    @Test
    @JsName("typedGetterTest_1")
    fun `as{Type} should be equal or similar to the value supplied`() =
        Types.forEach { targetType ->
            Types.compatibleWith(targetType).forEach { compatibleType ->
                Values.forType(compatibleType).forEach { value ->

                    val entry = Entry(targetType, Names.shouldntThrow, value)

                    val getter = Entries.getterFor(targetType)
                    val expectedValue = convert(value) from compatibleType to targetType

                    // Make sure the getter returns that value.
                    entry.getter() shouldEqual expectedValue

                    // Make sure the corresponding "component" function also returns that value.
                    entry.component3() shouldEqual expectedValue
                }
            }
        }

    @Test
    @JsName("typedGetterTest_2")
    fun `as{Type} should throw when used with wrong type`() = Types.forEach { targetType ->
        Types.compatibleWith(targetType).forEach { compatibleType ->
            Values.forType(compatibleType).forEach { value ->

                val entry = Entry(targetType, Names.shouldThrow, value)

                Types.allExcept(targetType).forEach { wrongType ->
                    val wrongGetter = Entries.getterFor(wrongType)

                    shouldThrow<IllegalStateException> {
                        entry.wrongGetter()
                    }
                }
            }
        }
    }
}