# me.nullicorn.ooze.nbt.test

This package (and those inside it) are not actual tests; just utilities for the tests in the `nbt`
package.

- `Assertions.kt` - Infix wrappers around some kotlin.test assertion functions, to make tests easier
  to read.
- `Converter.kt` - Mimics the library's type-conversion behavior for testing the library's behavior
  in such cases.
    - Conversion refers to the implicit conversion of values that use the wrong runtime type, given
      their NBT `Type`.
    - e.g. when a `kotlin.Byte` is passed to a function where a `TAG_Long` is expected, the library
      automatically converts the value to a Long via the `toLong()` method.
    - The same is also available for numeric array types (`TAG_Byte_Array`, `TAG_Int_Array`,
      and `TAG_Long_Array`), where every element in the array would be converted using the
      appropriate `toX` method.
- `Entries.kt` - Data for tests involving the `Entry` class.
- `Names.kt` - Data for testing tag names, specifically in compounds.
- `Types.kt` - Data for tests involving the `Type` enum.
- `Values.kt` - Stress-test data for NBT values. All values come from the `data` package.