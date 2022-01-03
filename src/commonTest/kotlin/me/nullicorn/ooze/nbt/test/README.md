# me.nullicorn.ooze.nbt.test

This package (and those inside it) are not actual tests; just utilities for the tests in the `nbt`
package.

- `Config` - Project-wide configuration for Kotest.
- `Compatibility.kt` - Kotest extensions for data-driven testing using the `data` package.
- `Converter.kt` - Mimics the library's type-conversion behavior for testing the library's behavior
  in such cases.
    - Conversion refers to the implicit conversion of values that use the wrong runtime type, given
      their NBT `Type`.
    - e.g. when a `kotlin.Byte` is passed to a function where a `TAG_Long` is expected, the library
      automatically converts the value to a Long via the `toLong()` method.
    - The same is also available for numeric array types (`TAG_Byte_Array`, `TAG_Int_Array`,
      and `TAG_Long_Array`), where every element in the array would be converted using the
      appropriate `toX` method.