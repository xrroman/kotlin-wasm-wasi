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
    description = "Compiles and runs the Kotlin/Wasm WASI binary using Wasmtime or Node.js"

    val isWindows = System.getProperty("os.name").lowercase().contains("windows")

    val wasmFile = layout.buildDirectory
        .file("compileSync/wasmWasi/main/productionExecutable/optimized/kotlin-wasm-wasi-wasm-wasi.wasm")
        .get().asFile

    if (isWindows) {
        commandLine(
            "node",
            "--experimental-wasi-unstable-preview1",
            "${projectDir}/run.mjs"
        )
        standardInput = System.`in`
    } else {
        commandLine("wasmtime", "--dir=.", wasmFile.absolutePath)
    }
}