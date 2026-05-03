# kotlin-wasm-wasi

A Kotlin/Wasm project that compiles to a WebAssembly binary targeting the WASI (WebAssembly System Interface) environment.

Upon execution, the program reads lines from `stdin` in a loop and echoes them back to `stdout` with a `Wasm received:` prefix.

## How it works

Kotlin/Wasm does not run on a JVM, so standard I/O APIs like `readLine()` are unavailable.
Instead, stdin is read byte-by-byte via the WASI `fd_read` syscall using Kotlin's `UnsafeWasmMemoryApi`
to manipulate WebAssembly linear memory directly.

The `.wasm` binary is executed by Node.js through a custom `run.mjs` entry point that wires WASI stdin/stdout.

## Requirements

- JDK 17+
- Node.js 18+
- Git

## Run

### Linux / Mac

```bash
./gradlew runWasm
```

### Windows (PowerShell)

```powershell
$env:JAVA_HOME = "<path-to-your-jdk>"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
.\gradlew.bat runWasm
```

## Example

Hello

Wasm received: Hello

World

Wasm received: World

Press `Ctrl+C` or `Ctrl+D` to exit.

## Tech stack

- Kotlin/Wasm 2.1.20 with `kotlin("multiplatform")` plugin
- Target: `wasmWasi` with Node.js runtime
- Build: Gradle 9.2.1