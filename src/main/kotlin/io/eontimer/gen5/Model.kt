package io.eontimer.gen5

import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Model(
    storedSettings: StoredSettings = StoredSettings()
) {
    val mode = SimpleObjectProperty(storedSettings.mode)
    val targetDelay = SimpleLongProperty(storedSettings.targetDelay)
    val targetSecond = SimpleLongProperty(storedSettings.targetSecond)
    val targetAdvances = SimpleLongProperty(storedSettings.targetAdvances)
    val calibration = SimpleLongProperty(storedSettings.calibration)
    val entralinkCalibration = SimpleLongProperty(storedSettings.entralinkCalibration)
    val frameCalibration = SimpleLongProperty(storedSettings.frameCalibration)
    val secondHit = SimpleLongProperty()
    val delayHit = SimpleLongProperty()
    val advancesHit = SimpleLongProperty()
}