package io.eontimer.gen4

data class StoredSettings(
    val targetDelay: Long = Defaults.TARGET_DELAY,
    val targetSecond: Long = Defaults.TARGET_SECOND,
    val calibratedDelay: Long = Defaults.CALIBRATED_DELAY,
    val calibratedSecond: Long = Defaults.CALIBRATED_SECOND
) {
    constructor(
        model: Model
    ) : this(
        targetDelay = model.targetDelay.get(),
        targetSecond = model.targetSecond.get(),
        calibratedDelay = model.calibratedDelay.get(),
        calibratedSecond = model.calibratedSecond.get()
    )
}
