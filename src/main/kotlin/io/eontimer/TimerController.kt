package io.eontimer

import io.eontimer.util.getStage
import io.eontimer.util.isFinite
import javafx.application.Platform
import kotlin.time.Duration

interface TimerController<ModelT, TimerFactoryT : ControllerTimerFactory> : TimerStateAware {
    val model: ModelT
    val timerFactory: TimerFactoryT
    val timerTab: TimerTab

    fun calibrate()
}

fun <ModelT, TimerFactoryT : ControllerTimerFactory> TimerController<ModelT, TimerFactoryT>.resetTimerState() {
    val stages = timerFactory.stages
    val firstStage = stages.getStage(0)
    val currentElapsed = when (firstStage.isFinite) {
        true -> firstStage
        false -> Duration.ZERO
    }

    Platform.runLater {
        state.stages.set(stages)
        state.stageIndex.set(0)
        state.elapsed.set(currentElapsed)
    }
}