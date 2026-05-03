@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

plugins {
    kotlin("multiplatform") version "2.1.20"
}

repositories {
    mavenCentral()
}

kotlin {
    wasmWasi {
        nodejs()
        binaries.executable()
    }
}

tasks.register("runWasm") {
    dependsOn("wasmWasiNodeProductionRun")
    group = "application"
    description = "Compiles and runs the Kotlin/Wasm WASI binary using Node.js"

    doLast {
        val isWindows = System.getProperty("os.name").lowercase().contains("windows")
        val node = if (isWindows) "node.exe" else "node"

        ProcessBuilder(
            node,
            "--experimental-wasi-unstable-preview1",
            "${projectDir}/run.mjs"
        )
            .inheritIO()
            .start()
            .waitFor()
    }
}