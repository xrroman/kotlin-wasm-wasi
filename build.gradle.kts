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

tasks.register<Exec>("runWasm") {
    group = "application"
    description = "Runs the Wasm/WASI binary using Node.js"

    dependsOn("compileProductionExecutableKotlinWasmWasiOptimize")

    // Forwards terminal input to the Wasm process
    standardInput = System.`in`

    commandLine(
        "node",
        "--experimental-wasi-unstable-preview1",
        "${projectDir}/run.mjs"
    )
}