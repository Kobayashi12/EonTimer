package io.eontimer.audio

import io.github.landerlyoung.jenny.NativeClass

internal class Deleter(
    private val rawAddress: Long
) {
    init {
        require(rawAddress != 0L) { "rawAddress for DeleteFn cannot be NULL" }
    }

    operator fun invoke(pointer: Pointer) =
        Native.delete(rawAddress, pointer.rawAddress)

    @NativeClass(namespace = "DeleteFn")
    object Native {
        @JvmStatic
        internal external fun delete(rawAddress: Long, pointer: Long)
    }
}