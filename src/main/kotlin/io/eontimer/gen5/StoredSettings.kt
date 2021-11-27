package io.eontimer.gen5

data class StoredSettings(
    val mode: Mode = Defaults.MODE,
    val targetDelay: Long = Defaults.TARGET_DELAY,
    val targetSecond: Long = Defaults.TARGET_SECOND,
    val targetAdvances: Long = Defaults.TARGET_ADVANCES,
    val calibration: Long = Defaults.CALIBRATION,
    val entralinkCalibration: Long = Defaults.ENTRALINK_CALIBRATION,
    val frameCalibration: Long = Defaults.FRAME_CALIBRATION
) {
    constructor(
        model: Model
    ) : this(
        mode = model.mode.get(),
        targetDelay = model.targetDelay.get(),
        targetSecond = model.targetSecond.get(),
        targetAdvances = model.targetAdvances.get(),
        calibration = model.calibration.get(),
        entralinkCalibration = model.entralinkCalibration.get(),
        frameCalibration = model.frameCalibration.get()
    )
}
