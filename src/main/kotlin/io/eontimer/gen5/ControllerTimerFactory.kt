package io.eontimer.gen5

import io.eontimer.Calibrator
import io.eontimer.ControllerTimerFactory
import io.eontimer.timer.factory.DelayTimerFactory
import io.eontimer.timer.factory.EnhancedEntralinkTimerFactory
import io.eontimer.timer.factory.EntralinkTimerFactory
import io.eontimer.timer.factory.SecondTimerFactory
import io.eontimer.util.javafx.plusAssign
import org.springframework.stereotype.Component
import kotlin.time.Duration

@Component("gen5ControllerTimerFactory")
class ControllerTimerFactory(
    private val model: Model,
    private val delayTimerFactory: DelayTimerFactory,
    private val secondTimerFactory: SecondTimerFactory,
    private val entralinkTimerFactory: EntralinkTimerFactory,
    private val enhancedEntralinkTimerFactory: EnhancedEntralinkTimerFactory,
    private val calibrator: Calibrator,
) : ControllerTimerFactory {
    private val delayCalibration: Long
        get() = delayTimerFactory.calibrate(model.targetDelay.get(), model.delayHit.get())
    private val secondCalibration: Long
        get() = secondTimerFactory.calibrate(model.targetSecond.get(), model.secondHit.get())
    private val entralinkCalibration: Long
        get() = entralinkTimerFactory.calibrate(model.targetDelay.get(), model.delayHit.get() - secondCalibration)

//    @PostConstruct
//    private fun initialize() {
//        model.mode.subscribe { resetTimerState() }
//        model.targetDelay.subscribe { resetTimerState() }
//        model.targetSecond.subscribe { resetTimerState() }
//        model.targetAdvances.subscribe { resetTimerState() }
//        model.calibration.subscribe { resetTimerState() }
//        model.entralinkCalibration.subscribe { resetTimerState() }
//        model.frameCalibration.subscribe { resetTimerState() }
//    }

    override val stages: List<Duration>
        get() = when (model.mode.get()!!) {
            Mode.STANDARD ->
                secondTimerFactory.createStages(
                    SecondTimerFactory.Params(
                        targetSecond = model.targetSecond.get(),
                        calibration = calibrator.calibrateToMillis(model.calibration.get())
                    )
                )
            Mode.C_GEAR ->
                delayTimerFactory.createStages(
                    DelayTimerFactory.Params(
                        targetDelay = model.targetDelay.get(),
                        targetSecond = model.targetSecond.get(),
                        calibration = calibrator.calibrateToMillis(model.calibration.get())
                    )
                )
            Mode.ENTRALINK ->
                entralinkTimerFactory.createStages(
                    EntralinkTimerFactory.Params(
                        targetDelay = model.targetDelay.get(),
                        targetSecond = model.targetSecond.get(),
                        entralinkCalibration = calibrator.calibrateToMillis(model.entralinkCalibration.get()),
                        calibration = calibrator.calibrateToMillis(model.calibration.get()),
                    )
                )
            Mode.ENHANCED_ENTRALINK ->
                enhancedEntralinkTimerFactory.createStages(
                    EnhancedEntralinkTimerFactory.Params(
                        targetDelay = model.targetDelay.get(),
                        targetSecond = model.targetSecond.get(),
                        targetAdvances = model.targetAdvances.get(),
                        calibration = calibrator.calibrateToMillis(model.calibration.get()),
                        entralinkCalibration = calibrator.calibrateToMillis(model.entralinkCalibration.get()),
                        frameCalibration = model.frameCalibration.get()
                    )
                )
        }

    override fun calibrate() {
        when (model.mode.get()!!) {
            Mode.STANDARD -> {
                model.calibration += calibrator.calibrateToDelays(secondCalibration)
            }
            Mode.C_GEAR -> {
                model.calibration += calibrator.calibrateToDelays(delayCalibration)
            }
            Mode.ENTRALINK -> {
                model.calibration += calibrator.calibrateToDelays(secondCalibration)
                model.entralinkCalibration += calibrator.calibrateToDelays(entralinkCalibration)
            }
            Mode.ENHANCED_ENTRALINK -> {
                model.calibration += calibrator.calibrateToDelays(secondCalibration)
                model.entralinkCalibration += calibrator.calibrateToDelays(entralinkCalibration)
                model.frameCalibration += enhancedEntralinkTimerFactory.calibrate(
                    model.targetAdvances.get(),
                    model.advancesHit.get()
                )
            }
        }
    }
}