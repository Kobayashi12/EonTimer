package io.eontimer.timer.factory

import io.eontimer.util.milliseconds
import io.eontimer.util.toBuilder
import org.springframework.stereotype.Service
import kotlin.time.Duration
import kotlin.math.roundToLong

@Service
class EnhancedEntralinkTimerFactory(
    private val entralinkTimer: EntralinkTimerFactory
) : TimerFactory<EnhancedEntralinkTimerFactory.Params> {
    override fun createStages(params: Params) =
        entralinkTimer.createStages(
            EntralinkTimerFactory.Params(
                targetDelay = params.targetDelay,
                targetSecond = params.targetSecond,
                entralinkCalibration = params.entralinkCalibration,
                calibration = params.calibration,
            )
        ).toBuilder {
            add(
                stage3(
                    params.targetAdvances,
                    params.frameCalibration
                )
            )
        }

    private fun stage3(targetAdvances: Long, frameCalibration: Long): Duration {
        return ((targetAdvances / ENTRALINK_FRAME_RATE).roundToLong() * 1000 + frameCalibration)
            .milliseconds
    }

    fun calibrate(targetAdvances: Long, actualAdvances: Long): Long {
        return ((targetAdvances - actualAdvances) / ENTRALINK_FRAME_RATE).roundToLong() * 1000

        // TODO: 4/1/19 - determine if this is still needed
        // val npcRate = 1.0 / calibrationService.toMillis(32)
        // return Math.round((targetAdvances - actualAdvances) / (Gen5TimerConstants.ENTRALINK_FRAME_RATE + (npcCount * npcRate))) * 1000
    }

    data class Params(
        val targetDelay: Long,
        val targetSecond: Long,
        val targetAdvances: Long,
        val entralinkCalibration: Long,
        val frameCalibration: Long,
        val calibration: Long
    )

    companion object {
        const val ENTRALINK_FRAME_RATE = 0.837148929
    }
}