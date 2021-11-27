package io.eontimer.model

import io.eontimer.model.settings.ActionSettings
import io.eontimer.model.settings.TimerSettings
import io.eontimer.model.timer.CustomTimer
import io.eontimer.gen3.Model
import io.eontimer.gen4.Gen4Timer
import io.eontimer.model.timer.Gen5Timer
import io.eontimer.model.timer.TimerConstants
import io.eontimer.model.timer.TimerType
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty

data class ApplicationModel(
    val gen3: Model = Model(),
    val gen4: Gen4Timer = Gen4Timer(),
    val gen5: Gen5Timer = Gen5Timer(),
    val custom: CustomTimer = CustomTimer(),
    val actionSettings: ActionSettings = ActionSettings(),
    val timerSettings: TimerSettings = TimerSettings()
) {
    val selectedTimerTypeProperty: ObjectProperty<TimerType> = SimpleObjectProperty(TimerConstants.DEFAULT_TIMER_TYPE)
    var selectedTimerType: TimerType by selectedTimerTypeProperty
}