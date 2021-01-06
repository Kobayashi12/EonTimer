package io.github.dylmeadows.eontimer.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.dylmeadows.eontimer.model.settings.ActionSettings
import io.github.dylmeadows.eontimer.model.settings.TimerSettings
import io.github.dylmeadows.eontimer.model.timer.*
import io.github.dylmeadows.commonkt.javafx.beans.property.getValue
import io.github.dylmeadows.commonkt.javafx.beans.property.setValue
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty

class ApplicationModel {
    var gen3 = Gen3TimerModel()
    var gen4 = Gen4TimerModel()
    var gen5 = Gen5TimerModel()
    var custom = CustomTimerModel()
    var actionSettings = ActionSettings()
    var timerSettings = TimerSettings()

    @JsonIgnore
    val selectedTimerTypeProperty: ObjectProperty<TimerType> = SimpleObjectProperty(TimerConstants.DEFAULT_TIMER_TYPE)
    var selectedTimerType: TimerType by selectedTimerTypeProperty
}