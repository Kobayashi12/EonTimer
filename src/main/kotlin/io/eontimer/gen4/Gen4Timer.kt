package io.eontimer.gen4

import javafx.beans.property.SimpleLongProperty

class Gen4Timer {
    val targetDelay = SimpleLongProperty(Defaults.TARGET_DELAY)
    val targetSecond = SimpleLongProperty(Defaults.TARGET_SECOND)
    val calibratedDelay = SimpleLongProperty(Defaults.CALIBRATED_DELAY)
    val calibratedSecond = SimpleLongProperty(Defaults.CALIBRATED_SECOND)
    val delayHit = SimpleLongProperty()

    object Defaults {
        const val TARGET_DELAY = 600L
        const val TARGET_SECOND = 50L
        const val CALIBRATED_DELAY = 500L
        const val CALIBRATED_SECOND = 14L
    }
}