plugins {
    kotlin("multiplatform") version "1.6.20-M1-40"
}

group = "me.nullicorn.ooze"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        binaries.executable()

        nodejs {
            testTask {
                useMocha {
                    timeout = "10000"
                }
            }
        }

        browser {
            testTask {
                useMocha {
                    timeout = "10000"
                }
            }
        }
    }

    // Suppressed b/c intellisense mistakes these for being unused.
    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }

        val commonMain by getting {}
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {}
        val jvmTest by getting {}

        val jsMain by getting {
            dependencies {
                // DEFLATE and gzip in JavaScript, via pako.
                // https://github.com/nodeca/pako
                implementation(npm("pako", "2.0.4"))
                implementation(npm("@types/pako", "1.0.3", generateExternals = true))
            }
        }
        val jsTest by getting {}
    }
}