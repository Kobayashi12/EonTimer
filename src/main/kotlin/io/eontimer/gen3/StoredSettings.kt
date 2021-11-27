package io.eontimer.gen3

data class StoredSettings(
    val mode: Mode = Defaults.MODE,
    val preTimer: Long = Defaults.PRE_TIMER,
    val targetFrame: Long = Defaults.TARGET_FRAME,
    val calibration: Long = Defaults.CALIBRATION
) {
    constructor(
        model: Model
    ) : this(
        mode = model.mode.get(),
        preTimer = model.preTimer.get(),
        targetFrame = model.targetFrame.get(),
        calibration = model.calibration.get()
    )
}
