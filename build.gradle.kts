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
    dependsOn("compileProductionExecutableKotlinWasmWasiOptimize")
    group = "application"

    // Esto es vital: permite que lo que escribas en la consola llegue a Wasm
    standardInput = System.`in`

    commandLine(
        "node",
        "--experimental-wasi-unstable-preview1",
        "${projectDir}/run.mjs"
    )
}