package io.eontimer.custom

import io.eontimer.ControllerTimerFactory
import org.springframework.stereotype.Component
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Component("customControllerTimerFactory")
class ControllerTimerFactory(
    private val model: Model
) : ControllerTimerFactory {
    override val stages: List<Duration> get() = model.stages.map { it.milliseconds }

    override fun calibrate() = Unit
}