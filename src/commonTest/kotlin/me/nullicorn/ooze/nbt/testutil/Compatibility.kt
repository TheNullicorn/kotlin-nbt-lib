package me.nullicorn.ooze.nbt.testutil

import io.kotest.core.names.TestName
import io.kotest.core.spec.style.scopes.ContainerScope
import io.kotest.core.spec.style.scopes.ShouldSpecContainerScope
import io.kotest.core.test.TestScope
import me.nullicorn.ooze.nbt.*
import me.nullicorn.ooze.nbt.testutil.data.*

typealias TestContainer = ShouldSpecContainerScope

typealias TypedTest = suspend TestContainer.(Type) -> Unit

/**
 * Runs a [test] in a new [context][ShouldSpecContainerScope.context] for each NBT [Type].
 *
 * Each test run will be supplied the current [Type].
 *
 * @param[targetName] The name of the parameter/field that the type is for.
 * e.g. when testing [TagList], this might be `contentType`.
 * @param[test] The test to run for every known [Type].
 */
suspend fun TestContainer.withTypes(targetName: String, test: TypedTest) {
    for (targetType in Types.all) {
        context("$targetName=$targetType") {
            TestContainer(this).test(targetType)
        }
    }
}

typealias CompatibilityTest = suspend TestContainer.(Any, Any) -> Unit

/**
 * Runs a [test] in a new [context][ShouldSpecContainerScope.context] for every sample value
 * compatible with an NBT [Type].
 *
 * - "Compatible" means that a value is either already the correct type, or it is the wrong type,
 * but it is convertable to the correct type.
 *    - e.g. [kotlin.Byte] --> [Type.BYTE] (no change)
 *    - e.g. [kotlin.Int] --> [Type.BYTE] (converted via Int.toByte())
 *    - e.g. [kotlin.ByteArray] --> [Type.LONG_ARRAY] (each byte converted via Byte.toLong())
 * - Each test run will be supplied a value [compatible with][convert] the [targetType].
 *
 * @param[targetType] The NBT type that all supplied values will be compatible with.
 * @param[test] The test to run for every sample value compatible with the [targetType].
 */
suspend fun TestContainer.withCompatibleValues(targetType: Type, test: CompatibilityTest) {
    for (compatibleType in Types.compatibleWith(targetType)) {

        context("accept $compatibleType values") {
            for (compatibleValue in Values.forType(compatibleType)) {

                registerTest("value=$compatibleValue") {
                    val convertedValue = convert(compatibleValue) from compatibleType to targetType
                    TestContainer(this).test(compatibleValue, convertedValue)
                }
            }
        }
    }
}

typealias IncompatibilityTest = suspend TestContainer.(Any) -> Unit

/**
 * Does the same thing as [withCompatibleValues], but the supplied values are guaranteed to be
 * **incompatible** with the [targetType].
 */
suspend fun TestContainer.withIncompatibleValues(targetType: Type, test: IncompatibilityTest) {
    for (incompatibleType in Types.incompatibleWith(targetType)) {

        context("reject $incompatibleType values") {
            for (incompatibleValue in Values.forType(incompatibleType)) {

                registerTest("value=$incompatibleValue") {
                    TestContainer(this).test(incompatibleValue)
                }
            }
        }
    }
}

typealias Test = suspend TestScope.() -> Unit

private suspend fun ContainerScope.registerTest(name: String, test: Test) =
    registerTest(
        // Max test name length should be 50 chars. Longer names can make the IDE very laggy.
        name = TestName(name.truncated(50)),
        disabled = false,
        config = null,
        test = test,
    )

/**
 * Ensures that the string is [maxLength] characters long, at most.
 *
 * For longer strings, any characters at indices after [maxLength - 1] will be removed, then the
 * last three characters will be replaced with dots "...".
 *
 * If the string is shorter or equal to the [maxLength] the string itself is returned. Otherwise,
 * the returned string will be a new instance.
 */
private fun String.truncated(maxLength: Int) = if (length <= maxLength) this else {
    substring(0, maxLength - 3) + "..."
}