package io.github.dylmeadows.eontimer.util

import io.github.dylmeadows.commonkt.core.time.minutes
import io.github.dylmeadows.eontimer.model.settings.TimerSettings
import javafx.scene.control.Label
import java.time.Duration

var Label.isActive: Boolean
    get() = this.styleClass.contains("active")
    set(value) {
        when (value) {
            true -> styleClass.add("active")
            false -> styleClass.remove("active")
        }
    }

fun Long.toMinimumLength(): Long {
    var normalized = this
    while (normalized < TimerSettings.MINIMUM_LENGTH)
        normalized += 1L.minutes.toMillis()
    return normalized
}

fun List<Duration>.getStage(index: Int): Duration {
    return if (index < size) get(index) else Duration.ZERO
}
