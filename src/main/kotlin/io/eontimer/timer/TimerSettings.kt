package io.eontimer.timer

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class TimerSettings(
    storedSettings: TimerStoredSettings = TimerStoredSettings()
) {
    val console = SimpleObjectProperty(storedSettings.console)
    val refreshInterval = SimpleLongProperty(storedSettings.refreshInterval)
    val precisionCalibration = SimpleBooleanProperty(storedSettings.precisionCalibrationMode)

    object Defaults {
        val CONSOLE = Console.NDS
        const val PRECISION_CALIBRATION_MODE = false
        const val REFRESH_INTERVAL = 8L
    }
}