package io.eontimer.time

@JvmInline
value class Instant private constructor(
    internal val nanoseconds: ULong
) {
    companion object {
        fun now(): Instant =
            Instant(System.nanoTime().toULong())
    }
}
