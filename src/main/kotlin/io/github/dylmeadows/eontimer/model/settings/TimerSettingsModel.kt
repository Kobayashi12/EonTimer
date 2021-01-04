package io.github.dylmeadows.eontimer.model.settings

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.*

class TimerSettingsModel {
    @JsonIgnore
    val consoleProperty: ObjectProperty<Console> = SimpleObjectProperty(TimerSettingsConstants.DEFAULT_CONSOLE)
    var console: Console by consoleProperty

    @JsonIgnore
    val refreshIntervalProperty: LongProperty = SimpleLongProperty(TimerSettingsConstants.DEFAULT_REFRESH_INTERVAL)
    var refreshInterval by refreshIntervalProperty

    @JsonIgnore
    val precisionCalibrationModeProperty: BooleanProperty =
        SimpleBooleanProperty(TimerSettingsConstants.DEFAULT_PRECISION_CALIBRATION_MODE)
    var precisionCalibrationMode by precisionCalibrationModeProperty
}