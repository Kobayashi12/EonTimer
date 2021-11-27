package io.eontimer.timer

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Settings(
    storedSettings: StoredSettings = StoredSettings()
) {
    val console = SimpleObjectProperty(storedSettings.console)
    val refreshInterval = SimpleLongProperty(storedSettings.refreshInterval)
    val precisionCalibrationMode = SimpleBooleanProperty(storedSettings.precisionCalibrationMode)
}