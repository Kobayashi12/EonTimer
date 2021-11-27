package io.eontimer.gen4

import io.eontimer.model.TimerState
import io.eontimer.service.CalibrationService
import io.eontimer.service.factory.TimerFactory
import io.eontimer.service.factory.timer.DelayTimerFactory
import io.eontimer.service.factory.update
import io.eontimer.util.javafx.plusAssign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component
@ExperimentalCoroutinesApi
class Gen4TimerFactory(
    private val gen4Timer: Gen4Timer,
    private val timerState: TimerState,
    private val delayTimerFactory: DelayTimerFactory,
    private val calibrationService: CalibrationService,
    private val coroutineScope: CoroutineScope
) : TimerFactory {
    private val calibration: Long
        get() = calibrationService.createCalibration(gen4Timer.calibratedDelay.get(), gen4Timer.calibratedSecond.get())

    @PostConstruct
    private fun initialize() {
        listOf(
            gen4Timer.targetDelay,
            gen4Timer.targetSecond,
            gen4Timer.calibratedDelay,
            gen4Timer.calibratedSecond
        ).forEach {
            coroutineScope.launch {
                it.asFlow().collect {
                    timerState.update(stages)
                }
            }
        }
    }

    override val stages: List<Duration>
        get() = delayTimerFactory.createStages(
            gen4Timer.targetSecond.get(),
            gen4Timer.targetDelay.get(),
            calibration
        )

    override fun calibrate() {
        gen4Timer.calibratedDelay +=
            calibrationService.calibrateToDelays(
                delayTimerFactory.calibrate(
                    gen4Timer.targetDelay.get(),
                    gen4Timer.delayHit.get()
                )
            )
    }
}