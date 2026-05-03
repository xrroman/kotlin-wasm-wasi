import { WASI } from 'wasi';
import { argv, env } from 'node:process';
import { readFileSync } from 'node:fs';
import { fileURLToPath, resolve } from 'node:url';
import { dirname, join } from 'node:path';

const __dirname = dirname(fileURLToPath(import.meta.url));

const wasmPath = join(
    __dirname,
    'build/compileSync/wasmWasi/main/productionExecutable/optimized/kotlin-wasm-wasi-wasm-wasi.wasm'
);

const wasi = new WASI({
    version: 'preview1',
    args: argv,
    env,
    stdin: 0,   // file descriptor 0 = stdin
    stdout: 1,  // file descriptor 1 = stdout
    stderr: 2,
});

const wasmBuffer = readFileSync(wasmPath);
const wasmModule = new WebAssembly.Module(wasmBuffer);
const wasmInstance = new WebAssembly.Instance(wasmModule, wasi.getImportObject());

wasi.initialize(wasmInstance);
wasmInstance.exports.main();