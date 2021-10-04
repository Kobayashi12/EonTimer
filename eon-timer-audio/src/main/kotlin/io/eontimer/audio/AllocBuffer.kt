package io.eontimer.audio

import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.reflect.KProperty

internal class AllocBuffer {
    val pointer: Long get() = directBuffer[POINTER_IDX]
    val deleter: Long get() = directBuffer[DELETER_IDX]
    val directBuffer = ByteBuffer.allocateDirect(2 * Long.SIZE_BYTES)
        .order(ByteOrder.nativeOrder())
        .asLongBuffer()

    companion object {
        private const val POINTER_IDX = 0
        private const val DELETER_IDX = 1

        private val buffers = ThreadLocal.withInitial { AllocBuffer() }
        val buffer: AllocBuffer get() = buffers.get()

        operator fun getValue(thisRef: Any?, property: KProperty<*>): AllocBuffer = buffer
    }
}