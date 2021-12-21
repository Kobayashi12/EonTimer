package io.eontimer.config

import io.eontimer.model.timer.TimerConstants
import io.eontimer.model.timer.TimerTab
import io.eontimer.action.StoredSettings as ActionStoredSettings
import io.eontimer.custom.StoredSettings as CustomStoredSettings
import io.eontimer.gen3.StoredSettings as Gen3StoredSettings
import io.eontimer.gen4.StoredSettings as Gen4StoredSettings
import io.eontimer.gen5.StoredSettings as Gen5StoredSettings
import io.eontimer.timer.StoredSettings as TimerStoredSettings

data class StoredSettings(
    val gen3: Gen3StoredSettings = Gen3StoredSettings(),
    val gen4: Gen4StoredSettings = Gen4StoredSettings(),
    val gen5: Gen5StoredSettings = Gen5StoredSettings(),
    val custom: CustomStoredSettings = CustomStoredSettings(),
    val actionSettings: ActionStoredSettings = ActionStoredSettings(),
    val timerSettings: TimerStoredSettings = TimerStoredSettings(),
    val selectedTimerTab: TimerTab = TimerConstants.DEFAULT_TIMER_TAB
)
