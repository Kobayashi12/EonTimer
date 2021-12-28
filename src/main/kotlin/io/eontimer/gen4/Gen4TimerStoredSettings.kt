package io.eontimer.gen4

data class Gen4TimerStoredSettings(
    val targetDelay: Long = Gen4TimerModel.Defaults.TARGET_DELAY,
    val targetSecond: Long = Gen4TimerModel.Defaults.TARGET_SECOND,
    val calibratedDelay: Long = Gen4TimerModel.Defaults.CALIBRATED_DELAY,
    val calibratedSecond: Long = Gen4TimerModel.Defaults.CALIBRATED_SECOND
) {
    constructor(
        model: Gen4TimerModel
    ) : this(
        targetDelay = model.targetDelay.get(),
        targetSecond = model.targetSecond.get(),
        calibratedDelay = model.calibratedDelay.get(),
        calibratedSecond = model.calibratedSecond.get()
    )
}
