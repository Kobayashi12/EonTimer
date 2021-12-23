package io.eontimer.timer.factory

import io.eontimer.Calibrator
import io.eontimer.Constants
import io.eontimer.util.milliseconds
import io.eontimer.util.toMinimumLength
import org.springframework.stereotype.Service
import kotlin.math.absoluteValue
import kotlin.time.Duration

@Service
class DelayTimerFactory(
    private val secondTimer: SecondTimerFactory,
    private val calibrator: Calibrator
) : TimerFactory<DelayTimerFactory.Params> {
    override fun createStages(params: Params) =
        listOf(
            stage1(params.targetSecond, params.targetDelay, params.calibration),
            stage2(params.targetDelay, params.calibration)
        )

    private fun stage1(targetSecond: Long, targetDelay: Long, calibration: Long): Duration {
        val stages = secondTimer.createStages(
            SecondTimerFactory.Params(
                targetSecond = targetSecond,
                calibration = calibration
            )
        )
        return (stages[0].inWholeMilliseconds - calibrator.toMillis(targetDelay))
            .toMinimumLength()
            .milliseconds
    }

    private fun stage2(targetDelay: Long, calibration: Long) =
        (calibrator.toMillis(targetDelay) - calibration).milliseconds

    fun calibrate(targetDelay: Long, delayHit: Long): Long {
        val delta = calibrator.toMillis(delayHit) - calibrator.toMillis(targetDelay)
        return when (delta.absoluteValue <= Constants.CLOSE_THRESHOLD) {
            true -> (Constants.CLOSE_UPDATE_FACTOR * delta).toLong()
            false -> Constants.UPDATE_FACTOR.toLong() * delta
        }
    }

    data class Params(
        val targetSecond: Long,
        val targetDelay: Long,
        val calibration: Long
    )
}