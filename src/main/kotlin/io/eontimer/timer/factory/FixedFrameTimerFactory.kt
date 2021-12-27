package io.eontimer.timer.factory

import io.eontimer.Calibrator
import org.springframework.stereotype.Service
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Service
class FixedFrameTimerFactory(
    private val calibrator: Calibrator
) : TimerFactory<FixedFrameTimerFactory.Params> {
    override fun createStages(params: Params) =
        listOf(
            stage1(params.preTimer),
            stage2(params.targetFrame, params.calibration)
        )

    private fun stage1(preTimer: Long): Duration =
        preTimer.milliseconds

    private fun stage2(targetFrame: Long, calibration: Long): Duration {
        return (calibrator.toMillis(targetFrame) + calibration)
            .milliseconds
    }

    fun calibrate(targetFrame: Long, frameHit: Long): Long =
        calibrator.toMillis(targetFrame - frameHit)

    data class Params(
        val preTimer: Long,
        val targetFrame: Long,
        val calibration: Long
    )
}