package io.eontimer.audio

internal class DeleteFn(
    private val rawAddress: Long
) {
    init {
        require(rawAddress != 0L) { "rawAddress for DeleteFn cannot be NULL" }
    }

    operator fun invoke(pointer: Pointer) {
        delete(rawAddress, pointer.rawAddress)
    }

    companion object {
        @JvmStatic
        private external fun delete(rawAddress: Long, pointer: Long)
    }
}