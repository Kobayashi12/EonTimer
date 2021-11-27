package io.eontimer.gen3

import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Model(
    storedSettings: StoredSettings = StoredSettings()
) {
    val mode = SimpleObjectProperty(storedSettings.mode)
    val preTimer = SimpleLongProperty(storedSettings.preTimer)
    val targetFrame = SimpleLongProperty(storedSettings.targetFrame)
    val calibration = SimpleLongProperty(storedSettings.calibration)
    val frameHit = SimpleLongProperty()
}