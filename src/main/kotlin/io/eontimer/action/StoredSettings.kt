package io.eontimer.action

import io.eontimer.util.javafx.toHex

data class StoredSettings(
    val mode: ActionMode = ActionSettings.Defaults.MODE,
    val color: String = ActionSettings.Defaults.COLOR.toHex(),
    val sound: Sound = ActionSettings.Defaults.SOUND,
    val interval: Int = ActionSettings.Defaults.INTERVAL,
    val count: Int = ActionSettings.Defaults.COUNT
) {
    constructor(
        actionSettings: ActionSettings
    ) : this(
        mode = actionSettings.mode.get(),
        color = actionSettings.color.get().toHex(),
        sound = actionSettings.sound.get(),
        interval = actionSettings.interval.get(),
        count = actionSettings.count.get()
    )
}
