package io.eontimer.gen4

import javafx.beans.property.SimpleLongProperty

class Model(
    storedSettings: StoredSettings = StoredSettings()
) {
    val targetDelay = SimpleLongProperty(storedSettings.targetDelay)
    val targetSecond = SimpleLongProperty(storedSettings.targetSecond)
    val calibratedDelay = SimpleLongProperty(storedSettings.calibratedDelay)
    val calibratedSecond = SimpleLongProperty(storedSettings.calibratedSecond)
    val delayHit = SimpleLongProperty()
}