package io.eontimer

import io.eontimer.timer.Settings
import io.eontimer.util.javafx.getValue
import org.springframework.stereotype.Service
import kotlin.math.roundToLong

@Service
class Calibrator(
    timerSettings: Settings
) {
    private val console by timerSettings.console
    private val precisionCalibration by timerSettings.precisionCalibration

    fun toMillis(delays: Long): Long =
        (delays * console.frameRate).roundToLong()

    fun toDelays(millis: Long): Long =
        (millis / console.frameRate).roundToLong()

    fun createCalibration(delay: Long, second: Long): Long =
        toMillis(delay - toDelays(second * 1000))

    fun calibrateToMillis(value: Long) =
        when (precisionCalibration) {
            true -> value
            false -> toMillis(value)
        }

    fun calibrateToDelays(value: Long) =
        when (precisionCalibration) {
            true -> value
            false -> toDelays(value)
        }
}
