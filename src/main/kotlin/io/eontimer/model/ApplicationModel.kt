package io.eontimer.model

import io.eontimer.model.timer.TimerConstants
import io.eontimer.model.timer.TimerType
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import io.eontimer.custom.Model as CustomModel
import io.eontimer.gen3.Model as Gen3Model
import io.eontimer.gen4.Model as Gen4Model
import io.eontimer.gen5.Model as Gen5Model
import io.eontimer.action.Settings as ActionSettings
import io.eontimer.timer.Settings as TimerSettings

data class ApplicationModel(
    val gen3: Gen3Model = Gen3Model(),
    val gen4: Gen4Model = Gen4Model(),
    val gen5: Gen5Model = Gen5Model(),
    val custom: CustomModel = CustomModel(),
    val actionSettings: ActionSettings = ActionSettings(),
    val timerSettings: TimerSettings = TimerSettings()
) {
    val selectedTimerTypeProperty: ObjectProperty<TimerType> = SimpleObjectProperty(TimerConstants.DEFAULT_TIMER_TYPE)
    var selectedTimerType: TimerType by selectedTimerTypeProperty
}