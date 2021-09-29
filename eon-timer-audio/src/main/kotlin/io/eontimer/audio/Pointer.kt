package io.eontimer.audio

import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean

internal class Pointer private constructor(
    internal val rawAddress: Long,
    private val deleteFn: DeleteFn
) : Closeable {
    private val active = AtomicBoolean(true)

    constructor(
        allocBuffer: AllocBuffer
    ) : this(
        allocBuffer.pointer,
        DeleteFn(allocBuffer.deleteFn)
    )

    init {
        require(rawAddress != 0L) { "rawAddress for Pointer cannot be NULL" }
    }

    override fun close() {
        if (active.compareAndSet(true, false)) {
            deleteFn(this)
        }
    }
}