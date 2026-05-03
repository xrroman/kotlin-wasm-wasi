@file:OptIn(kotlin.wasm.unsafe.UnsafeWasmMemoryApi::class)

import kotlin.wasm.unsafe.*

/**
 * Reads a single byte from stdin (fd=0) using the WASI fd_read syscall.
 */
@WasmImport("wasi_snapshot_preview1", "fd_read")
external fun fdRead(fd: Int, iovs: Int, iovsLen: Int, nreadPtr: Int): Int

fun main() {
    while (true) {
        val line = readLineFromStdin()
        if (!line.isNullOrEmpty()) {
            println("Wasm received: $line")
        }
    }
}

fun readLineFromStdin(): String? {
    val bytes = mutableListOf<Byte>()
    while (true) {
        val byte = readByteFromStdin() ?: return if (bytes.isEmpty()) null else bytes.toByteArray().decodeToString()
        if (byte == '\n'.code.toByte()) return bytes.toByteArray().decodeToString().trimEnd('\r')
        bytes.add(byte)
    }
}

fun readByteFromStdin(): Byte? {
    return withScopedMemoryAllocator { allocator ->
        val buf = allocator.allocate(1)
        val iov = allocator.allocate(8)
        val nread = allocator.allocate(4)

        iov.storeInt(buf.address.toInt())
        (iov + 4).storeInt(1)

        val errno = fdRead(0, iov.address.toInt(), 1, nread.address.toInt())
        val bytesRead = nread.loadInt()

        if (errno == 0 && bytesRead > 0) buf.loadByte() else null
    }
}