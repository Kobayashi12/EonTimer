package io.eontimer

import javafx.application.Platform

interface TimerController<ModelT, TimerFactoryT : ControllerTimerFactory> : TimerStateAware {
    val model: ModelT
    val timerFactory: TimerFactoryT
    val timerTab: TimerTab

    fun calibrate()
}

fun TimerStateAware.resetTimerState(timerFactory: AggregateControllerTimerFactory) {
    val stages = timerFactory.stages
    Platform.runLater {
        state.stagesProperty.set(stages)
    }
}

fun <ModelT, TimerFactoryT : ControllerTimerFactory> TimerController<ModelT, TimerFactoryT>.resetTimerState() {
    val stages = timerFactory.stages
    Platform.runLater {
        state.stagesProperty.set(stages)
    }
}