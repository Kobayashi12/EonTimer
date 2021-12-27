package io.eontimer.timer.factory

import org.springframework.stereotype.Service
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Service
class EntralinkTimerFactory(
    private val delayTimer: DelayTimerFactory
) : TimerFactory<EntralinkTimerFactory.Params> {
    override fun createStages(params: Params): List<Duration> {
        val stages = delayTimer.createStages(
            DelayTimerFactory.Params(
                targetSecond = params.targetSecond,
                targetDelay = params.targetDelay,
                calibration = params.calibration
            )
        )
        return listOf(
            stage1(stages),
            stage2(stages, params.entralinkCalibration)
        )
    }

    fun calibrate(targetDelay: Long, delayHit: Long): Long {
        return delayTimer.calibrate(targetDelay, delayHit)
    }

    private fun stage1(stages: List<Duration>): Duration {
        return stages[0] + 250L.milliseconds
    }

    private fun stage2(stages: List<Duration>, entralinkCalibration: Long): Duration {
        return stages[1] - entralinkCalibration.milliseconds
    }

    data class Params(
        val targetDelay: Long,
        val targetSecond: Long,
        val entralinkCalibration: Long,
        val calibration: Long
    )
}
