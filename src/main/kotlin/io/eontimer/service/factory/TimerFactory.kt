package io.eontimer.service.factory

import io.eontimer.model.TimerState
import io.eontimer.util.getStage
import io.eontimer.util.isIndefinite
import io.eontimer.util.sum
import java.time.Duration

interface TimerFactory {
    val stages: List<Duration>
    fun calibrate()
}

internal fun TimerState.update(stages: List<Duration>) {
    val stage = stages.firstOrNull() ?: Duration.ZERO

    currentStage.set(stage)
    currentRemaining.set(if (stage.isIndefinite) Duration.ZERO else stage)
    nextStage.set(stages.elementAtOrNull(1) ?: Duration.ZERO)
    totalTime.set(stages.sum())
}

