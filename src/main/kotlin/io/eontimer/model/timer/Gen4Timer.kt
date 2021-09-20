package io.eontimer.model.timer

import io.eontimer.util.javafx.Choice
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Gen4Timer {
    val modeProperty: ObjectProperty<Mode> = SimpleObjectProperty(Defaults.MODE)
    val calibratedDelayProperty: LongProperty = SimpleLongProperty(Defaults.CALIBRATED_DELAY)
    val calibratedSecondProperty: LongProperty = SimpleLongProperty(Defaults.CALIBRATED_SECOND)
    val targetDelayProperty: LongProperty = SimpleLongProperty(Defaults.TARGET_DELAY)
    val targetSecondProperty: LongProperty = SimpleLongProperty(Defaults.TARGET_SECOND)
    val delayHitProperty: LongProperty = SimpleLongProperty()

    var mode: Mode by modeProperty
    var calibratedDelay by calibratedDelayProperty
    var calibratedSecond by calibratedSecondProperty
    var targetDelay by targetDelayProperty
    var targetSecond by targetSecondProperty
    var delayHit by delayHitProperty

    object Defaults {
        // @formatter:off
        @JvmField val MODE = Mode.STANDARD
        const val CALIBRATED_DELAY = 500L
        const val CALIBRATED_SECOND = 14L
        const val TARGET_DELAY = 600L
        const val TARGET_SECOND = 50L
        // @formatter:on
    }

    enum class Mode(
        override val displayName: String
    ) : Choice {
        STANDARD("Standard")
    }
}