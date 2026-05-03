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

    sourceSets {
        val wasmWasiMain by getting
        val wasmWasiTest by getting
    }
}

tasks.register<Exec>("runWasm") {
    dependsOn("wasmWasiNodeProductionRun")
    group = "application"
    description = "Compiles and runs the Kotlin/Wasm WASI binary using Node.js"

    commandLine(
        "node",
        "--experimental-wasi-unstable-preview1",
        "${projectDir}/run.mjs"
    )

    standardInput = System.`in`
}