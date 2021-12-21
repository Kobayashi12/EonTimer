package io.eontimer.gen3

import io.eontimer.model.TimerState
import io.eontimer.service.factory.TimerFactory
import io.eontimer.service.factory.resetTimerState
import io.eontimer.service.factory.timer.FixedFrameTimerFactory
import io.eontimer.service.factory.timer.VariableFrameTimerFactory
import io.eontimer.util.javafx.easybind.monadic
import io.eontimer.util.javafx.easybind.subscribe
import io.eontimer.util.javafx.plusAssign
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component("gen3TimerFactory")
@ExperimentalCoroutinesApi
class TimerFactory(
    private val model: Model,
    override val timerState: TimerState,
    private val fixedFrameTimerFactory: FixedFrameTimerFactory,
    private val variableFrameTimerFactory: VariableFrameTimerFactory,
) : TimerFactory {
    @PostConstruct
    private fun initialize() {
        model.mode.subscribe { resetTimerState() }
        model.preTimer.subscribe { resetTimerState() }
        model.calibration.subscribe { resetTimerState() }

        model.mode
            .monadic()
            .filter { it == Mode.STANDARD }
            .flatMap { model.targetFrame }
            .subscribe { _ ->
                resetTimerState()
            }
    }

    override val stages: List<Duration>
        get() = when (model.mode.get()!!) {
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