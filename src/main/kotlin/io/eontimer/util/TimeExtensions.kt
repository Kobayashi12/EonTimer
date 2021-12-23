package io.eontimer.util

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

const val MINIMUM_LENGTH = 14000L
private val ONE_MINUTE = 1.minutes.inWholeMilliseconds

fun List<Duration>.sum(): Duration =
    when (contains(Duration.INFINITE)) {
        false -> reduceRight { duration, acc -> acc + duration }
        true -> Duration.INFINITE
    }

fun Long.toMinimumLength(): Long {
    var normalized = this
    while (normalized < MINIMUM_LENGTH)
        normalized += ONE_MINUTE
    return normalized
}

@Suppress("NOTHING_TO_INLINE")
inline fun List<Duration>.getStage(index: Int): Duration =
    elementAtOrElse(index) { Duration.ZERO }
