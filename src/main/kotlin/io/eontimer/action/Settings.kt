package io.eontimer.action

import io.eontimer.util.milliseconds
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.scene.paint.Color
import org.fxmisc.easybind.EasyBind
import kotlin.time.Duration

class Settings(
    storedSettings: StoredSettings = StoredSettings()
) {
    val mode = SimpleObjectProperty(storedSettings.mode)
    val color = SimpleObjectProperty(Color.web(storedSettings.color))
    val sound = SimpleObjectProperty(storedSettings.sound)
    val interval = SimpleIntegerProperty(storedSettings.interval)
    val count = SimpleIntegerProperty(storedSettings.count)

    val actionInstants: ObservableValue<List<Duration>> =
        EasyBind.combine(count, interval) { count, interval ->
            (0 until count.toLong())
                .reversed()
                .asSequence()
                .map { (it * interval.toLong()).milliseconds }
                .toList()
        }
}