package io.github.dylmeadows.eontimer.model.timer

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.dylmeadows.commonkt.javafx.beans.property.getValue
import io.github.dylmeadows.commonkt.javafx.beans.property.setValue
import javafx.beans.property.*

class Gen3TimerModel {
    @JsonIgnore
    val modeProperty: ObjectProperty<Gen3TimerMode> = SimpleObjectProperty(Gen3TimerConstants.DEFAULT_MODE)
    var mode: Gen3TimerMode by modeProperty

    @JsonIgnore
    val calibrationProperty: LongProperty = SimpleLongProperty(Gen3TimerConstants.DEFAULT_CALIBRATION)
    var calibration by calibrationProperty

    @JsonIgnore
    val preTimerProperty: LongProperty = SimpleLongProperty(Gen3TimerConstants.DEFAULT_PRE_TIMER)
    var preTimer by preTimerProperty

    @JsonIgnore
    val targetFrameProperty: LongProperty = SimpleLongProperty(Gen3TimerConstants.DEFAULT_TARGET_FRAME)
    var targetFrame by targetFrameProperty

    @JsonIgnore
    val frameHitProperty: LongProperty = SimpleLongProperty()

    @delegate:JsonIgnore
    var frameHit by frameHitProperty
}