package io.eontimer.config

import io.eontimer.Constants
import io.eontimer.TimerTab
import io.eontimer.action.ActionStoredSettings as ActionStoredSettings
import io.eontimer.custom.CustomTimerStoredSettings as CustomStoredSettings
import io.eontimer.gen3.Gen3TimerStoredSettings as Gen3StoredSettings
import io.eontimer.gen4.Gen4TimerStoredSettings as Gen4StoredSettings
import io.eontimer.gen5.Gen5TimerStoredSettings as Gen5StoredSettings
import io.eontimer.timer.TimerStoredSettings as TimerStoredSettings

data class StoredSettings(
    val gen3: Gen3StoredSettings = Gen3StoredSettings(),
    val gen4: Gen4StoredSettings = Gen4StoredSettings(),
    val gen5: Gen5StoredSettings = Gen5StoredSettings(),
    val custom: CustomStoredSettings = CustomStoredSettings(),
    val actionSettings: ActionStoredSettings = ActionStoredSettings(),
    val timerSettings: TimerStoredSettings = TimerStoredSettings(),
    val selectedTimerTab: TimerTab = Constants.DEFAULT_TIMER_TAB
)
