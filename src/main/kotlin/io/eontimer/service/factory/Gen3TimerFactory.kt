package io.eontimer.service.factory

import io.eontimer.model.TimerState
import io.eontimer.model.timer.Gen3Timer
import io.eontimer.service.factory.timer.FixedFrameTimerFactory
import io.eontimer.service.factory.timer.VariableFrameTimerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component
@ExperimentalCoroutinesApi
class Gen3TimerFactory(
    private val gen3Timer: Gen3Timer,
    private val timerState: TimerState,
    private val fixedFrameTimerFactory: FixedFrameTimerFactory,
    private val variableFrameTimerFactory: VariableFrameTimerFactory,
    private val coroutineScope: CoroutineScope
) : TimerFactory {
    @PostConstruct
    private fun initialize() {
        coroutineScope.launch {
            listOf(
                gen3Timer.modeProperty,
                gen3Timer.preTimerProperty,
                gen3Timer.calibrationProperty
            ).forEach {
                it.asFlow().collect {
                    timerState.update(stages)
                }
            }
            gen3Timer.targetFrameProperty.asFlow()
                .filter { gen3Timer.mode == Gen3Timer.Mode.STANDARD }
                .collect { timerState.update(stages) }
        }
    }

    override val stages: List<Duration>
        get() {
            return when (gen3Timer.mode) {
                Gen3Timer.Mode.STANDARD ->
                    fixedFrameTimerFactory.createStages(
                        gen3Timer.preTimer,
                        gen3Timer.targetFrame,
                        gen3Timer.calibration
                    )
                Gen3Timer.Mode.VARIABLE_TARGET ->
                    variableFrameTimerFactory.createStages(
                        gen3Timer.preTimer
                    )
            }
        }

    override fun calibrate() {
        // NOTE: VariableFrameTimer is essentially a FixedFrameTimer
        // just with a floating target frame value. Therefore, the
        // calibration process is the same for both.
        gen3Timer.calibration +=
            fixedFrameTimerFactory.calibrate(
                gen3Timer.targetFrame,
                gen3Timer.frameHit
            )
    }
}