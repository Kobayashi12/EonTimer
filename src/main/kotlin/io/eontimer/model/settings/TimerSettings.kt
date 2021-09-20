package io.eontimer.model.settings

import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import javafx.beans.property.BooleanProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class TimerSettings {
    val consoleProperty: ObjectProperty<Console> = SimpleObjectProperty(Defaults.CONSOLE)
    val refreshIntervalProperty: LongProperty = SimpleLongProperty(Defaults.REFRESH_INTERVAL)
    val precisionCalibrationModeProperty: BooleanProperty = SimpleBooleanProperty(Defaults.PRECISION_CALIBRATION_MODE)

    var console: Console by consoleProperty
    var refreshInterval by refreshIntervalProperty
    var precisionCalibrationMode by precisionCalibrationModeProperty

    object Defaults {
        // @formatter:off
        @JvmField val CONSOLE = Console.NDS
        const val PRECISION_CALIBRATION_MODE = false
        const val REFRESH_INTERVAL = 8L
        // @formatter:on
    }
}