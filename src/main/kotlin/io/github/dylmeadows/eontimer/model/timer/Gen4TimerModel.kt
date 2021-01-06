package io.github.dylmeadows.eontimer.model.timer

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.dylmeadows.commonkt.javafx.beans.property.getValue
import io.github.dylmeadows.commonkt.javafx.beans.property.setValue
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty

class Gen4TimerModel {
    @JsonIgnore
    val modeProperty: ObjectProperty<Gen4TimerMode> = SimpleObjectProperty(Gen4TimerConstants.DEFAULT_MODE)
    var mode: Gen4TimerMode by modeProperty

    @JsonIgnore
    val calibratedDelayProperty: LongProperty = SimpleLongProperty(Gen4TimerConstants.DEFAULT_CALIBRATED_DELAY)
    var calibratedDelay by calibratedDelayProperty

    @JsonIgnore
    val calibratedSecondProperty: LongProperty = SimpleLongProperty(Gen4TimerConstants.DEFAULT_CALIBRATED_SECOND)
    var calibratedSecond by calibratedSecondProperty

    @JsonIgnore
    val targetDelayProperty: LongProperty = SimpleLongProperty(Gen4TimerConstants.DEFAULT_TARGET_DELAY)
    var targetDelay by targetDelayProperty

    @JsonIgnore
    val targetSecondProperty: LongProperty = SimpleLongProperty(Gen4TimerConstants.DEFAULT_TARGET_SECOND)
    var targetSecond by targetSecondProperty

    @JsonIgnore
    val delayHitProperty: LongProperty = SimpleLongProperty()

    @delegate:JsonIgnore
    var delayHit by delayHitProperty
}