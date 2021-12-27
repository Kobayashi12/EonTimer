package io.eontimer.gen4

import javafx.beans.property.SimpleLongProperty

class Gen4TimerModel(
    storedSettings: StoredSettings = StoredSettings()
) {
    val targetDelay = SimpleLongProperty(storedSettings.targetDelay)
    val targetSecond = SimpleLongProperty(storedSettings.targetSecond)
    val calibratedDelay = SimpleLongProperty(storedSettings.calibratedDelay)
    val calibratedSecond = SimpleLongProperty(storedSettings.calibratedSecond)
    val delayHit = SimpleLongProperty()

    object Defaults {
        const val TARGET_DELAY = 600L
        const val TARGET_SECOND = 50L
        const val CALIBRATED_DELAY = 500L
        const val CALIBRATED_SECOND = 14L
    }
}