package io.eontimer

import kotlin.time.Duration

interface ControllerTimerFactory {
    val stages: List<Duration>
    fun calibrate()
}
