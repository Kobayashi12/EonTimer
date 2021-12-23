package io.eontimer.timer.factory

import kotlin.time.Duration

interface TimerFactory<ParamsT> {
    fun createStages(params: ParamsT): List<Duration>
}