package io.eontimer

import io.eontimer.model.TimerStateAware
import io.eontimer.model.timer.TimerTab

interface TimerController<ModelT> : TimerStateAware {
    val model: ModelT
    val timerTab: TimerTab

    fun calibrate()
}