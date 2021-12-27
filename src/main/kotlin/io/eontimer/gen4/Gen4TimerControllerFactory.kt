package io.eontimer.gen4

import io.eontimer.Calibrator
import io.eontimer.ControllerTimerFactory
import io.eontimer.timer.factory.DelayTimerFactory
import io.eontimer.util.javafx.plusAssign
import org.springframework.stereotype.Component
import kotlin.time.Duration

@Component("gen4ControllerTimerFactory")
class Gen4TimerControllerFactory(
    private val model: Gen4TimerModel,
    private val delayTimerFactory: DelayTimerFactory,
    private val calibrator: Calibrator,
) : ControllerTimerFactory {
    override val stages: List<Duration>
        get() = delayTimerFactory.createStages(
            DelayTimerFactory.Params(
                targetDelay = model.targetDelay.get(),
                targetSecond = model.targetSecond.get(),
                calibration = calibration
            )
        )
    private val calibration: Long
        get() = calibrator.createCalibration(model.calibratedDelay.get(), model.calibratedSecond.get())

//    @PostConstruct
//    private fun initialize() {
//        model.targetDelay.subscribe { resetTimerState() }
//        model.targetSecond.subscribe { resetTimerState() }
//        model.calibratedDelay.subscribe { resetTimerState() }
//        model.calibratedSecond.subscribe { resetTimerState() }
//    }

    override fun calibrate() {
        model.calibratedDelay +=
            calibrator.calibrateToDelays(
                delayTimerFactory.calibrate(
                    model.targetDelay.get(),
                    model.delayHit.get()
                )
            )
    }
}