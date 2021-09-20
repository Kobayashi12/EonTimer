package io.eontimer.service.factory.timer

import io.eontimer.util.milliseconds
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.math.roundToLong

@Service
class EnhancedEntralinkTimerFactory(
    private val entralinkTimer: EntralinkTimerFactory
) {
    fun createStages(targetSecond: Long, targetDelay: Long, targetAdvances: Long,
                     calibration: Long, entralinkCalibration: Long, frameCalibration: Long): List<Duration> {
        val stages = entralinkTimer.createStages(targetSecond, targetDelay, calibration, entralinkCalibration)
        return listOf(stages[0], stages[1], stage3(targetAdvances, frameCalibration))
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

    companion object {
        const val ENTRALINK_FRAME_RATE = 0.837148929
    }
}