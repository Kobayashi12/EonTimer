package io.github.dylmeadows.eontimer.model.settings

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.dylmeadows.commonkt.javafx.beans.property.getValue
import io.github.dylmeadows.commonkt.javafx.beans.property.setValue
import javafx.beans.property.*

class TimerSettings {
    @JsonIgnore
    val consoleProperty: ObjectProperty<Console> = SimpleObjectProperty(DEFAULT_CONSOLE)
    var console: Console by consoleProperty

    @JsonIgnore
    val refreshIntervalProperty: LongProperty = SimpleLongProperty(DEFAULT_REFRESH_INTERVAL)
    var refreshInterval by refreshIntervalProperty

    @JsonIgnore
    val precisionCalibrationModeProperty: BooleanProperty = SimpleBooleanProperty(DEFAULT_PRECISION_CALIBRATION_MODE)
    var precisionCalibrationMode by precisionCalibrationModeProperty

    companion object {
        @JvmField
        val DEFAULT_CONSOLE = Console.NDS

        const val DEFAULT_PRECISION_CALIBRATION_MODE = false
        const val DEFAULT_REFRESH_INTERVAL = 8L
        const val MINIMUM_LENGTH = 14000L
    }
}