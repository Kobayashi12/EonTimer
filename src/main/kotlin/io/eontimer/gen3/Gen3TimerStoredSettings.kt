package io.eontimer.gen3

data class Gen3TimerStoredSettings(
    val mode: Gen3TimerMode = Gen3TimerModel.Defaults.MODE,
    val preTimer: Long = Gen3TimerModel.Defaults.PRE_TIMER,
    val targetFrame: Long = Gen3TimerModel.Defaults.TARGET_FRAME,
    val calibration: Long = Gen3TimerModel.Defaults.CALIBRATION
) {
    constructor(
        model: Gen3TimerModel
    ) : this(
        mode = model.mode.get(),
        preTimer = model.preTimer.get(),
        targetFrame = model.targetFrame.get(),
        calibration = model.calibration.get()
    )
}
