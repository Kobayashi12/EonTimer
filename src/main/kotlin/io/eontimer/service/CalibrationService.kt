package io.eontimer.service

import io.eontimer.timer.Settings
import org.springframework.stereotype.Service
import kotlin.math.roundToLong

@Service
class CalibrationService(
    private val timerSettings: Settings
) {
    private val console get() = timerSettings.console.get()

    fun toMillis(delays: Long): Long =
        (delays * console.frameRate).roundToLong()

    fun toDelays(millis: Long): Long =
        (millis / console.frameRate).roundToLong()

    fun createCalibration(delay: Long, second: Long): Long =
        toMillis(delay - toDelays(second * 1000))

    fun calibrateToMillis(value: Long): Long {
        return if (timerSettings.precisionCalibrationMode.get()) value else toMillis(value)
    }

    fun calibrateToDelays(value: Long): Long {
        return if (timerSettings.precisionCalibrationMode.get()) value else toDelays(value)
    }
}