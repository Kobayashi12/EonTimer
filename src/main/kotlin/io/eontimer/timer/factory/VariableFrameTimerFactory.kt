package io.eontimer.timer.factory

import org.springframework.stereotype.Service
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Service
class VariableFrameTimerFactory : TimerFactory<VariableFrameTimerFactory.Params> {
    override fun createStages(params: Params) =
        listOf(params.preTimer.milliseconds, Duration.INFINITE)

    data class Params(
        val preTimer: Long
    )
}