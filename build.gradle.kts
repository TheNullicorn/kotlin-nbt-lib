plugins {
    kotlin("multiplatform") version "1.6.20-M1-40"
    id("io.kotest.multiplatform") version "5.0.2"
}

group = "me.nullicorn.ooze"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
}

kotlin {
    val kotestVersion = "5.0.2"

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

        nodejs()

        browser()
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
                implementation("io.kotest:kotest-assertions-core:$kotestVersion")
                implementation("io.kotest:kotest-framework-engine:$kotestVersion")
                implementation("io.kotest:kotest-framework-datatest:$kotestVersion")
            }
        }

        val jvmMain by getting {}
        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
            }
        }

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