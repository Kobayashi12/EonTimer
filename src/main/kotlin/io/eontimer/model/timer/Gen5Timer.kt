package io.eontimer.model.timer

import io.eontimer.util.javafx.Choice
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import javafx.beans.property.LongProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Gen5Timer {
    val modeProperty = SimpleObjectProperty(Defaults.MODE)
    val calibrationProperty: LongProperty = SimpleLongProperty(Defaults.CALIBRATION)
    val targetDelayProperty: LongProperty = SimpleLongProperty(Defaults.TARGET_DELAY)
    val targetSecondProperty: LongProperty = SimpleLongProperty(Defaults.TARGET_SECOND)
    val entralinkCalibrationProperty: LongProperty = SimpleLongProperty(Defaults.ENTRALINK_CALIBRATION)
    val frameCalibrationProperty: LongProperty = SimpleLongProperty(Defaults.FRAME_CALIBRATION)
    val targetAdvancesProperty: LongProperty = SimpleLongProperty(Defaults.TARGET_ADVANCES)
    val secondHitProperty: LongProperty = SimpleLongProperty()
    val delayHitProperty: LongProperty = SimpleLongProperty()
    val actualAdvancesProperty: LongProperty = SimpleLongProperty()

    var mode: Mode by modeProperty
    var calibration by calibrationProperty
    var targetDelay by targetDelayProperty
    var targetSecond by targetSecondProperty
    var entralinkCalibration by entralinkCalibrationProperty
    var frameCalibration by frameCalibrationProperty
    var targetAdvances by targetAdvancesProperty
    var secondHit by secondHitProperty
    var delayHit by delayHitProperty
    var actualAdvances by actualAdvancesProperty

    object Defaults {
        // @formatter:off
        @JvmField val MODE = Mode.STANDARD
        const val CALIBRATION = -95L
        const val TARGET_DELAY = 1200L
        const val TARGET_SECOND = 50L
        const val ENTRALINK_CALIBRATION = 256L
        const val FRAME_CALIBRATION = 0L
        const val TARGET_ADVANCES = 100L
        // @formatter:on
    }

    enum class Mode(
        override val displayName: String
    ) : Choice {
        STANDARD("Standard"),
        C_GEAR("C-Gear"),
        ENTRALINK("Entralink"),
        ENHANCED_ENTRALINK("Entralink+")
    }
}