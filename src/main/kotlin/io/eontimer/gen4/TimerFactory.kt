package io.eontimer.gen4

import io.eontimer.model.TimerState
import io.eontimer.service.CalibrationService
import io.eontimer.service.factory.TimerFactory
import io.eontimer.service.factory.resetTimerState
import io.eontimer.service.factory.timer.DelayTimerFactory
import io.eontimer.util.javafx.easybind.subscribe
import io.eontimer.util.javafx.plusAssign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component("gen4TimerFactory")
@ExperimentalCoroutinesApi
class TimerFactory(
    private val model: Model,
    override val timerState: TimerState,
    private val delayTimerFactory: DelayTimerFactory,
    private val calibrationService: CalibrationService,
    private val coroutineScope: CoroutineScope
) : TimerFactory {
    override val stages: List<Duration>
        get() = delayTimerFactory.createStages(
            model.targetSecond.get(),
            model.targetDelay.get(),
            calibration
        )
    private val calibration: Long
        get() = calibrationService.createCalibration(model.calibratedDelay.get(), model.calibratedSecond.get())

    @PostConstruct
    private fun initialize() {
        model.targetDelay.subscribe { resetTimerState() }
        model.targetSecond.subscribe { resetTimerState() }
        model.calibratedDelay.subscribe { resetTimerState() }
        model.calibratedSecond.subscribe { resetTimerState() }
    }

    override fun calibrate() {
        model.calibratedDelay +=
            calibrationService.calibrateToDelays(
                delayTimerFactory.calibrate(
                    model.targetDelay.get(),
                    model.delayHit.get()
                )
            )
    }
}