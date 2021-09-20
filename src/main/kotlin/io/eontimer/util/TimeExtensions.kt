package io.eontimer.util

import java.time.Duration
import java.time.Instant

val INDEFINITE = Duration.between(Instant.MIN, Instant.MAX)!!
val Duration.isIndefinite: Boolean get() = this == INDEFINITE
const val MINIMUM_LENGTH = 14000L

inline val Long.minutes: Duration get() = Duration.ofMinutes(this)
inline val Long.seconds: Duration get() = Duration.ofSeconds(this)
inline val Long.milliseconds: Duration get() = Duration.ofMillis(this)
inline val Long.nanoseconds: Duration get() = Duration.ofNanos(this)

fun List<Duration>.sum(): Duration =
    if (INDEFINITE !in this) {
        Duration.ofMillis(sumOf(Duration::toMillis))
    } else {
        INDEFINITE
    }

fun Long.toMinimumLength(): Long {
    var normalized = this
    while (normalized < MINIMUM_LENGTH)
        normalized += 1L.minutes.toMillis()
    return normalized
}

fun List<Duration>.getStage(index: Int): Duration {
    return if (index < size) get(index) else Duration.ZERO
}
