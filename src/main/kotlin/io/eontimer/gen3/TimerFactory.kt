package io.eontimer.gen3

import io.eontimer.model.TimerState
import io.eontimer.service.factory.TimerFactory
import io.eontimer.service.factory.timer.FixedFrameTimerFactory
import io.eontimer.service.factory.timer.VariableFrameTimerFactory
import io.eontimer.service.factory.update
import io.eontimer.util.javafx.plusAssign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component("gen3TimerFactory")
@ExperimentalCoroutinesApi
class TimerFactory(
    private val model: Model,
    private val timerState: TimerState,
    private val fixedFrameTimerFactory: FixedFrameTimerFactory,
    private val variableFrameTimerFactory: VariableFrameTimerFactory,
    private val coroutineScope: CoroutineScope
) : TimerFactory {
    @PostConstruct
    private fun initialize() {
        listOf(
            model.mode,
            model.preTimer,
            model.calibration
        ).forEach {
            coroutineScope.launch {
                it.asFlow()
                    .collect {
                        timerState.update(stages)
                    }
            }
        }
        coroutineScope.launch {
            model.targetFrame.asFlow()
                .filter { model.mode.get() == Mode.STANDARD }
                .collect { timerState.update(stages) }
        }
    }

    override val stages: List<Duration>
        get() {
            return when (model.mode.get()!!) {
                Mode.STANDARD ->
                    fixedFrameTimerFactory.createStages(
                        model.preTimer.get(),
                        model.targetFrame.get(),
                        model.calibration.get()
                    )
                Mode.VARIABLE_TARGET ->
                    variableFrameTimerFactory.createStages(
                        model.preTimer.get()
                    )
            }
        }

    override fun calibrate() {
        // NOTE: VariableFrameTimer is essentially a FixedFrameTimer
        // just with a floating target frame value. Therefore, the
        // calibration process is the same for both.
        model.calibration += fixedFrameTimerFactory.calibrate(
            model.targetFrame.get(),
            model.frameHit.get()
        )
    }
}