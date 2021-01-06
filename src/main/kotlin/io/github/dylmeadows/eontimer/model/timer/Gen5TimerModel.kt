package io.github.dylmeadows.eontimer.model.timer

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.dylmeadows.commonkt.javafx.beans.property.getValue
import io.github.dylmeadows.commonkt.javafx.beans.property.setValue
import javafx.beans.property.LongProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Gen5TimerModel {
    @JsonIgnore
    val modeProperty = SimpleObjectProperty(Gen5TimerConstants.DEFAULT_MODE)
    var mode: Gen5TimerMode by modeProperty

    @JsonIgnore
    val calibrationProperty: LongProperty = SimpleLongProperty(Gen5TimerConstants.DEFAULT_CALIBRATION)
    var calibration by calibrationProperty

    @JsonIgnore
    val targetDelayProperty: LongProperty = SimpleLongProperty(Gen5TimerConstants.DEFAULT_TARGET_DELAY)
    var targetDelay by targetDelayProperty

    @JsonIgnore
    val targetSecondProperty: LongProperty = SimpleLongProperty(Gen5TimerConstants.DEFAULT_TARGET_SECOND)
    var targetSecond by targetSecondProperty

    @JsonIgnore
    val entralinkCalibrationProperty: LongProperty =
        SimpleLongProperty(Gen5TimerConstants.DEFAULT_ENTRALINK_CALIBRATION)
    var entralinkCalibration by entralinkCalibrationProperty

    @JsonIgnore
    val frameCalibrationProperty: LongProperty = SimpleLongProperty(Gen5TimerConstants.DEFAULT_FRAME_CALIBRATION)
    var frameCalibration by frameCalibrationProperty

    @JsonIgnore
    val targetAdvancesProperty: LongProperty = SimpleLongProperty(Gen5TimerConstants.DEFAULT_TARGET_ADVANCES)
    var targetAdvances by targetAdvancesProperty

    @JsonIgnore
    val secondHitProperty: LongProperty = SimpleLongProperty()
    var secondHit by secondHitProperty

    @JsonIgnore
    val delayHitProperty: LongProperty = SimpleLongProperty()
    var delayHit by delayHitProperty

    @JsonIgnore
    val actualAdvancesProperty: LongProperty = SimpleLongProperty()
    @delegate:JsonIgnore
    var actualAdvances by actualAdvancesProperty
}