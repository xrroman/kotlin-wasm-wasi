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
    description = "Compiles and runs the Kotlin/Wasm WASI binary using Node.js"

    val isWindows = System.getProperty("os.name").lowercase().contains("windows")
    val node = if (isWindows) "node.exe" else "node"

    commandLine(
        node,
        "--experimental-wasi-unstable-preview1",
        "${projectDir}/run.mjs"
    )

    standardInput = System.`in`
}