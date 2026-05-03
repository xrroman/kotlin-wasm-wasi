import { WASI } from 'wasi';
import { argv, env } from 'node:process';
import { readFileSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { dirname, resolve } from 'node:path';

const __dirname = dirname(fileURLToPath(import.meta.url));
const wasmPath = resolve(__dirname, 'build/compileSync/wasmWasi/main/productionExecutable/optimized/kotlin-wasm-wasi-wasm-wasi.wasm');

const wasi = new WASI({
    version: 'preview1',
    args: argv,
    env,
    preopens: { '.': '.' }
});

const wasmBuffer = readFileSync(wasmPath);
const wasmModule = await WebAssembly.compile(wasmBuffer);
const wasmInstance = await WebAssembly.instantiate(wasmModule, wasi.getImportObject());

if (wasmInstance.exports._start) {
    wasi.start(wasmInstance);
} else {
    wasi.initialize(wasmInstance);
    wasmInstance.exports.main();
}