package me.nullicorn.ooze.nbt.test

import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

typealias Test = () -> Unit

inline fun <reified E : Throwable> shouldThrow(test: Test) {
    try {
        // Fail if any code runs after the test (meaning nothing thrown).
        test()
        throw AssertionError("Expected a ${E::class.simpleName} to be thrown, but nothing was")

    } catch (ignored: AssertionError) {
        // If an AssertionError was thrown inside try{}, ignore/rethrow it.
        throw ignored

    } catch (cause: Throwable) {
        // Fail if the thrown exception is the wrong type.
        if (cause !is E) throw AssertionError(
            "Expected a ${E::class.simpleName} to be thrown, but ${cause::class.simpleName} was"
        )
    }
}

fun shouldNotThrow(test: () -> Unit) {
    try {
        test()
    } catch (failure: AssertionError) {
        throw failure
    } catch (cause: Throwable) {
        throw AssertionError("Expected nothing to be thrown, but ${cause::class.simpleName} was")
    }
}

@Suppress("UNCHECKED_CAST")
infix fun Any?.shouldEqual(expected: Any?) = when (expected) {
    is ByteArray -> {
        assertIs<ByteArray>(this)
        assertContentEquals(expected, this)
    }

    is ShortArray -> {
        assertIs<ShortArray>(this)
        assertContentEquals(expected, this)
    }

    is IntArray -> {
        assertIs<IntArray>(this)
        assertContentEquals(expected, this)
    }

    is LongArray -> {
        assertIs<LongArray>(this)
        assertContentEquals(expected, this)
    }

    is FloatArray -> {
        assertIs<FloatArray>(this)
        assertContentEquals(expected, this)
    }

    is DoubleArray -> {
        assertIs<DoubleArray>(this)
        assertContentEquals(expected, this)
    }

    is CharArray -> {
        assertIs<CharArray>(this)
        assertContentEquals(expected, this)
    }

    is Array<*> -> {
        assertIs<Array<Any?>>(this)
        assertContentEquals(expected as Array<Any?>, this)
    }

    else -> assertEquals(expected, this)
}