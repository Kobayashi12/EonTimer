package io.eontimer.gen5

import io.eontimer.model.TimerState
import io.eontimer.service.CalibrationService
import io.eontimer.service.factory.TimerFactory
import io.eontimer.service.factory.timer.DelayTimerFactory
import io.eontimer.service.factory.timer.EnhancedEntralinkTimerFactory
import io.eontimer.service.factory.timer.EntralinkTimerFactory
import io.eontimer.service.factory.timer.SecondTimerFactory
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

@Component("gen5TimerFactory")
@ExperimentalCoroutinesApi
class TimerFactory(
    private val model: Model,
    private val timerState: TimerState,
    private val delayTimerFactory: DelayTimerFactory,
    private val secondTimerFactory: SecondTimerFactory,
    private val entralinkTimerFactory: EntralinkTimerFactory,
    private val enhancedEntralinkTimerFactory: EnhancedEntralinkTimerFactory,
    private val calibrationService: CalibrationService,
    private val coroutineScope: CoroutineScope
) : TimerFactory {
    private val delayCalibration: Long
        get() = delayTimerFactory.calibrate(model.targetDelay.get(), model.delayHit.get())

    private val secondCalibration: Long
        get() = secondTimerFactory.calibrate(model.targetSecond.get(), model.secondHit.get())

    private val entralinkCalibration: Long
        get() = entralinkTimerFactory.calibrate(model.targetDelay.get(), model.delayHit.get() - secondCalibration)

    private val advancesCalibration: Long
        get() = enhancedEntralinkTimerFactory.calibrate(model.targetAdvances.get(), model.advancesHit.get())

    @PostConstruct
    private fun initialize() {
        coroutineScope.launch {
            listOf(
                model.mode,
                model.calibration,
                model.targetDelay,
                model.targetSecond,
                model.entralinkCalibration,
                model.frameCalibration,
                model.targetAdvances
            ).forEach {
                it.asFlow().collect {
                    timerState.update(stages)
                }
            }
        }
    }

    override val stages: List<Duration>
        get() {
            return when (model.mode.get()) {
                Mode.STANDARD ->
                    secondTimerFactory.createStages(
                        model.targetSecond.get(),
                        calibrationService.calibrateToMillis(model.calibration.get()))
                Mode.C_GEAR ->
                    delayTimerFactory.createStages(
                        model.targetSecond.get(),
                        model.targetDelay.get(),
                        calibrationService.calibrateToMillis(model.calibration.get()))
                Mode.ENTRALINK ->
                    entralinkTimerFactory.createStages(
                        model.targetSecond.get(),
                        model.targetDelay.get(),
                        calibrationService.calibrateToMillis(model.calibration.get()),
                        calibrationService.calibrateToMillis(model.entralinkCalibration.get()))
                Mode.ENHANCED_ENTRALINK ->
                    enhancedEntralinkTimerFactory.createStages(
                        model.targetSecond.get(),
                        model.targetDelay.get(),
                        model.targetAdvances.get(),
                        calibrationService.calibrateToMillis(model.calibration.get()),
                        calibrationService.calibrateToMillis(model.entralinkCalibration.get()),
                        model.frameCalibration.get())
            }
        }

    override fun calibrate() {
        when (model.mode.get()) {
            Mode.STANDARD -> {
                model.calibration += calibrationService.calibrateToDelays(secondCalibration)
            }
            Mode.C_GEAR -> {
                model.calibration += calibrationService.calibrateToDelays(delayCalibration)
            }
            Mode.ENTRALINK -> {
                model.calibration += calibrationService.calibrateToDelays(secondCalibration)
                model.entralinkCalibration += calibrationService.calibrateToDelays(entralinkCalibration)
            }
            Mode.ENHANCED_ENTRALINK -> {
                model.calibration += calibrationService.calibrateToDelays(secondCalibration)
                model.entralinkCalibration += calibrationService.calibrateToDelays(entralinkCalibration)
                model.frameCalibration += advancesCalibration
            }
        }
    }
}