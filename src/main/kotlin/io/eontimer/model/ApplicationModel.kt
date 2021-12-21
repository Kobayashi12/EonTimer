package io.eontimer.model

import io.eontimer.config.StoredSettings
import io.eontimer.model.timer.TimerConstants
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
    val timerSettings: TimerSettings = TimerSettings(),
) {
    val selectedTimerTab = SimpleObjectProperty(TimerConstants.DEFAULT_TIMER_TAB)

    constructor(
        storedSettings: StoredSettings
    ) : this(
        gen3 = Gen3Model(storedSettings.gen3),
        gen4 = Gen4Model(storedSettings.gen4),
        gen5 = Gen5Model(storedSettings.gen5),
        custom = CustomModel(storedSettings.custom),
        actionSettings = ActionSettings(storedSettings.actionSettings),
        timerSettings = TimerSettings(storedSettings.timerSettings),
    ) {
        selectedTimerTab.set(storedSettings.selectedTimerTab)
    }
}