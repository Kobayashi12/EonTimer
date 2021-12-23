package io.eontimer.timer.factory

import io.eontimer.util.INDEFINITE
import io.eontimer.util.milliseconds
import org.springframework.stereotype.Service

@Service
class VariableFrameTimerFactory : TimerFactory<VariableFrameTimerFactory.Params> {
    override fun createStages(params: Params) =
        listOf(params.preTimer.milliseconds, INDEFINITE)

    data class Params(
        val preTimer: Long
    )
}