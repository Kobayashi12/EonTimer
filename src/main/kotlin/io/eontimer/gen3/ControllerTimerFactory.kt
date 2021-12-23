package io.eontimer.gen3

import io.eontimer.ControllerTimerFactory
import io.eontimer.timer.factory.FixedFrameTimerFactory
import io.eontimer.timer.factory.VariableFrameTimerFactory
import io.eontimer.util.javafx.plusAssign
import org.springframework.stereotype.Component
import kotlin.time.Duration


@Component("gen3ControllerTimerFactory")
class ControllerTimerFactory(
    private val model: Model,
    private val fixedFrameTimerFactory: FixedFrameTimerFactory,
    private val variableFrameTimerFactory: VariableFrameTimerFactory,
) : ControllerTimerFactory {
//    @PostConstruct
//    private fun initialize() {
//        model.mode.subscribe { resetTimerState() }
//        model.preTimer.subscribe { resetTimerState() }
//        model.calibration.subscribe { resetTimerState() }
//
//        model.mode
//            .monadic()
//            .filter { it == Mode.STANDARD }
//            .flatMap { model.targetFrame }
//            .subscribe { _ ->
//                resetTimerState()
//            }
//    }

    override val stages: List<Duration>
        get() = when (model.mode.get()!!) {
            Mode.STANDARD ->
                fixedFrameTimerFactory.createStages(
                    FixedFrameTimerFactory.Params(
                        preTimer = model.preTimer.get(),
                        targetFrame = model.targetFrame.get(),
                        calibration = model.calibration.get()
                    )
                )
            Mode.VARIABLE_TARGET ->
                variableFrameTimerFactory.createStages(
                    VariableFrameTimerFactory.Params(
                        preTimer = model.preTimer.get()
                    )
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