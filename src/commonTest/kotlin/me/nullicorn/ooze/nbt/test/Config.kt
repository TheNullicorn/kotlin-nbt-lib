// Suppressed because this config file is detected automatically by Kotest.
@file:Suppress("unused")

package me.nullicorn.ooze.nbt.test

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.names.DuplicateTestNameMode
import io.kotest.core.test.AssertionMode

class Config : AbstractProjectConfig() {
    // Include the "should" prefix before test names. It's not as readable without them.
    override val includeTestScopePrefixes = true

    // Hide warnings for test cases with the same name. Such tests should always be in different
    // scopes/contexts anyway.
    override val duplicateTestNameMode = DuplicateTestNameMode.Silent

    // Show a warning if a test case has no assertions.
    override val assertionMode = AssertionMode.Warn
}