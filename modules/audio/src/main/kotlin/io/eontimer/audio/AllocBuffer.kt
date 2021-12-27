package io.eontimer.audio

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.LongBuffer
import kotlin.reflect.KProperty

internal class AllocBuffer {
    val pointer: Long get() = directBuffer[POINTER_IDX]
    val deleteFn: Long get() = directBuffer[DELETE_FN_IDX]
    val directBuffer: LongBuffer = ByteBuffer.allocateDirect(2 * Long.SIZE_BYTES)
        .order(ByteOrder.nativeOrder())
        .asLongBuffer()

    companion object {
        private const val POINTER_IDX = 0
        private const val DELETE_FN_IDX = 1

        private val buffers = ThreadLocal.withInitial { AllocBuffer() }
        val buffer: AllocBuffer get() = buffers.get()

        operator fun getValue(thisRef: Any?, property: KProperty<*>): AllocBuffer = buffer
    }
}