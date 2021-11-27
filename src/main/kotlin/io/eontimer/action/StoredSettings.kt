package io.eontimer.action

import io.eontimer.util.javafx.toHex

data class StoredSettings(
    val mode: Mode = Defaults.MODE,
    val color: String = Defaults.COLOR.toHex(),
    val sound: Sound = Defaults.SOUND,
    val interval: Int = Defaults.INTERVAL,
    val count: Int = Defaults.COUNT
) {
    constructor(
        settings: Settings
    ) : this(
        mode = settings.mode.get(),
        color = settings.color.get().toHex(),
        sound = settings.sound.get(),
        interval = settings.interval.get(),
        count = settings.count.get()
    )
}
