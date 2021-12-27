package io.eontimer.gen3

import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Gen3TimerModel(
    storedSettings: StoredSettings = StoredSettings()
) {
    val mode = SimpleObjectProperty(this, "mode", storedSettings.mode)
    val preTimer = SimpleLongProperty(this, "preTimer", storedSettings.preTimer)
    val targetFrame = SimpleLongProperty(this, "targetFrame", storedSettings.targetFrame)
    val calibration = SimpleLongProperty(this, "calibration", storedSettings.calibration)
    val frameHit = SimpleLongProperty(this, "frameHit")

    object Defaults {
        val MODE = Gen3TimerMode.STANDARD
        const val PRE_TIMER = 5000L
        const val TARGET_FRAME = 1000L
        const val CALIBRATION = 0L
    }
}