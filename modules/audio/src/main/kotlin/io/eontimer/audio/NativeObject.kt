package io.eontimer.audio

import java.io.Closeable
import java.lang.ref.Cleaner

open class NativeObject internal constructor(
    init: Init
) : Closeable {
    internal val pointer: Pointer
    private val cleanable: Cleaner.Cleanable

    init {
        val allocBuffer by AllocBuffer
        this.pointer = init(allocBuffer)
        this.cleanable = cleaner.register(this, pointer::close)
    }

    override fun close() = cleanable.clean()

    companion object {
        private val cleaner = Cleaner.create()
    }

    internal fun interface Init {
        operator fun invoke(allocBuffer: AllocBuffer): Pointer
    }
}