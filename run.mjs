import { WASI } from 'wasi';
import { argv, env } from 'node:process';
import { readFileSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { dirname, resolve } from 'node:path';

const __dirname = dirname(fileURLToPath(import.meta.url));

// Usamos resolve para evitar problemas de rutas relativas en Unix
const wasmPath = resolve(__dirname, 'build/compileSync/wasmWasi/main/productionExecutable/optimized/kotlin-wasm-wasi-wasm-wasi.wasm');

const wasi = new WASI({
    version: 'preview1',
    args: argv,
    env,
    preopens: {
        '.': '.' // Esto es útil si luego quieres leer archivos del sistema
    }
});

const wasmBuffer = readFileSync(wasmPath);
const wasmModule = new WebAssembly.Module(wasmBuffer);
const wasmInstance = new WebAssembly.Instance(wasmModule, wasi.getImportObject());

// IMPORTANTE: En WASI Preview1 para Node, 'start' es lo que suele disparar el main
if (wasmInstance.exports._start) {
    wasi.start(wasmInstance);
} else {
    wasi.initialize(wasmInstance);
    wasmInstance.exports.main();
}