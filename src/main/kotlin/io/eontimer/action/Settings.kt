package io.eontimer.action

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color

class Settings(
    storedSettings: StoredSettings = StoredSettings()
) {
    val mode = SimpleObjectProperty(storedSettings.mode)
    val color = SimpleObjectProperty(Color.web(storedSettings.color))
    val sound = SimpleObjectProperty(storedSettings.sound)
    val interval = SimpleIntegerProperty(storedSettings.interval)
    val count = SimpleIntegerProperty(storedSettings.count)
}