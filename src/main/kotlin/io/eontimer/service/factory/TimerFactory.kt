package io.eontimer.service.factory

import io.eontimer.model.TimerStateAware
import io.eontimer.util.isIndefinite
import io.eontimer.util.sum
import javafx.application.Platform
import java.time.Duration

interface TimerFactory : TimerStateAware {
    val stages: List<Duration>
    fun calibrate()
}

fun TimerFactory.resetTimerState() {
    val firstStage = stages.elementAtOrNull(0) ?: Duration.ZERO
    val currentRemaining = if (firstStage.isIndefinite) Duration.ZERO else firstStage
    val secondStage = stages.elementAtOrNull(1) ?: Duration.ZERO
    val totalTime = stages.sum()

    Platform.runLater {
        timerState.currentStage.set(firstStage)
        timerState.currentRemaining.set(currentRemaining)
        timerState.nextStage.set(secondStage)
        timerState.totalTime.set(totalTime)
    }
}

