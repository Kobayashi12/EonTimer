package io.eontimer.service.factory

import io.eontimer.model.TimerState
import io.eontimer.model.timer.Gen5Timer
import io.eontimer.service.CalibrationService
import io.eontimer.service.factory.timer.DelayTimerFactory
import io.eontimer.service.factory.timer.EnhancedEntralinkTimerFactory
import io.eontimer.service.factory.timer.EntralinkTimerFactory
import io.eontimer.service.factory.timer.SecondTimerFactory
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
class Gen5TimerFactory(
    private val gen5Timer: Gen5Timer,
    private val timerState: TimerState,
    private val delayTimerFactory: DelayTimerFactory,
    private val secondTimerFactory: SecondTimerFactory,
    private val entralinkTimerFactory: EntralinkTimerFactory,
    private val enhancedEntralinkTimerFactory: EnhancedEntralinkTimerFactory,
    private val calibrationService: CalibrationService,
    private val coroutineScope: CoroutineScope
) : TimerFactory {
    private val delayCalibration: Long
        get() = delayTimerFactory.calibrate(gen5Timer.targetDelay, gen5Timer.delayHit)

    private val secondCalibration: Long
        get() = secondTimerFactory.calibrate(gen5Timer.targetSecond, gen5Timer.secondHit)

    private val entralinkCalibration: Long
        get() = entralinkTimerFactory.calibrate(gen5Timer.targetDelay, gen5Timer.delayHit - secondCalibration)

    private val advancesCalibration: Long
        get() = enhancedEntralinkTimerFactory.calibrate(gen5Timer.targetAdvances, gen5Timer.actualAdvances)

    @PostConstruct
    private fun initialize() {
        coroutineScope.launch {
            listOf(
                gen5Timer.modeProperty,
                gen5Timer.calibrationProperty,
                gen5Timer.targetDelayProperty,
                gen5Timer.targetSecondProperty,
                gen5Timer.entralinkCalibrationProperty,
                gen5Timer.frameCalibrationProperty,
                gen5Timer.targetAdvancesProperty
            ).forEach {
                it.asFlow().collect {
                    timerState.update(stages)
                }
            }
        }
    }

    override val stages: List<Duration>
        get() {
            return when (gen5Timer.mode) {
                Gen5Timer.Mode.STANDARD ->
                    secondTimerFactory.createStages(
                        gen5Timer.targetSecond,
                        calibrationService.calibrateToMillis(gen5Timer.calibration))
                Gen5Timer.Mode.C_GEAR ->
                    delayTimerFactory.createStages(
                        gen5Timer.targetSecond,
                        gen5Timer.targetDelay,
                        calibrationService.calibrateToMillis(gen5Timer.calibration))
                Gen5Timer.Mode.ENTRALINK ->
                    entralinkTimerFactory.createStages(
                        gen5Timer.targetSecond,
                        gen5Timer.targetDelay,
                        calibrationService.calibrateToMillis(gen5Timer.calibration),
                        calibrationService.calibrateToMillis(gen5Timer.entralinkCalibration))
                Gen5Timer.Mode.ENHANCED_ENTRALINK ->
                    enhancedEntralinkTimerFactory.createStages(
                        gen5Timer.targetSecond,
                        gen5Timer.targetDelay,
                        gen5Timer.targetAdvances,
                        calibrationService.calibrateToMillis(gen5Timer.calibration),
                        calibrationService.calibrateToMillis(gen5Timer.entralinkCalibration),
                        gen5Timer.frameCalibration)
            }
        }

    override fun calibrate() {
        when (gen5Timer.mode) {
            Gen5Timer.Mode.STANDARD -> {
                gen5Timer.calibration += calibrationService.calibrateToDelays(secondCalibration)
            }
            Gen5Timer.Mode.C_GEAR -> {
                gen5Timer.calibration += calibrationService.calibrateToDelays(delayCalibration)
            }
            Gen5Timer.Mode.ENTRALINK -> {
                gen5Timer.calibration += calibrationService.calibrateToDelays(secondCalibration)
                gen5Timer.entralinkCalibration += calibrationService.calibrateToDelays(entralinkCalibration)
            }
            Gen5Timer.Mode.ENHANCED_ENTRALINK -> {
                gen5Timer.calibration += calibrationService.calibrateToDelays(secondCalibration)
                gen5Timer.entralinkCalibration += calibrationService.calibrateToDelays(entralinkCalibration)
                gen5Timer.frameCalibration += advancesCalibration
            }
        }
    }
}