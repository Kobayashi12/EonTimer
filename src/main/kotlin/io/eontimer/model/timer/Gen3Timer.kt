package io.eontimer.model.timer

import io.eontimer.util.javafx.Choice
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Gen3Timer {
    val modeProperty: ObjectProperty<Mode> = SimpleObjectProperty(Defaults.MODE)
    val calibrationProperty: LongProperty = SimpleLongProperty(Defaults.CALIBRATION)
    val preTimerProperty: LongProperty = SimpleLongProperty(Defaults.PRE_TIMER)
    val targetFrameProperty: LongProperty = SimpleLongProperty(Defaults.TARGET_FRAME)
    val frameHitProperty: LongProperty = SimpleLongProperty()

    var mode: Mode by modeProperty
    var calibration by calibrationProperty
    var preTimer by preTimerProperty
    var targetFrame by targetFrameProperty
    var frameHit by frameHitProperty

    object Defaults {
        // @formatter:off
        @JvmField val MODE = Mode.STANDARD
        const val CALIBRATION = 0L
        const val PRE_TIMER = 5000L
        const val TARGET_FRAME = 1000L
        // @formatter:on
    }

    enum class Mode(
        override val displayName: String
    ) : Choice {
        STANDARD("Standard"),
        VARIABLE_TARGET("Variable Target")
    }
}