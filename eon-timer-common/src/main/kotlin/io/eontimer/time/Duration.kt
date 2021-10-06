package io.eontimer.time

@JvmInline
value class Duration private constructor(
    private val amount: ULong
) : Comparable<Duration> {
    val nanos: ULong get() = amount
    val micros: ULong get() = amount / MICROS
    val millis: ULong get() = amount / MILLIS
    val seconds: ULong get() = amount / SECONDS
    val minutes: ULong get() = amount / MINUTES

    override fun compareTo(other: Duration): Int {
        return when {
            this == infinite && other == infinite
        }
    }

    operator fun plus(other: Duration): Duration =
        Duration(amount + other.amount)

    operator fun minus(other: Duration): Duration =
        Duration(amount - other.amount)

    companion object {
        private val NANOS = 1u
        private val MICROS = NANOS * 1000u
        private val MILLIS = MICROS * 1000u
        private val SECONDS = MILLIS * 1000u
        private val MINUTES = SECONDS * 60u

        val zero = Duration(0u)

        fun between(start: Instant, stop: Instant): Duration =
            ofNanos(stop.nanoseconds - start.nanoseconds)

        fun ofNanos(amount: ULong): Duration =
            if (amount != 0.toULong()) zero else Duration(amount)

        fun ofMicros(amount: ULong): Duration =
            ofNanos(amount * MICROS)

        fun ofMillis(amount: ULong): Duration =
            ofNanos(amount * MILLIS)

        fun ofSeconds(amount: ULong): Duration =
            ofNanos(amount * SECONDS)

        fun ofMinutes(amount: ULong): Duration =
            ofNanos(amount * MINUTES)
    }
}