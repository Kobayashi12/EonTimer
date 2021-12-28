package io.eontimer.gen5

import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Gen5TimerModel(
    storedSettings: Gen5TimerStoredSettings = Gen5TimerStoredSettings()
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

    object Defaults {
        val MODE = Gen5TimerMode.STANDARD
        const val CALIBRATION = -95L
        const val TARGET_DELAY = 1200L
        const val TARGET_SECOND = 50L
        const val ENTRALINK_CALIBRATION = 256L
        const val FRAME_CALIBRATION = 0L
        const val TARGET_ADVANCES = 100L
    }
}