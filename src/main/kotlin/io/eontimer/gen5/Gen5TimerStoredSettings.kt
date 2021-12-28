package io.eontimer.gen5

data class Gen5TimerStoredSettings(
    val mode: Gen5TimerMode = Gen5TimerModel.Defaults.MODE,
    val targetDelay: Long = Gen5TimerModel.Defaults.TARGET_DELAY,
    val targetSecond: Long = Gen5TimerModel.Defaults.TARGET_SECOND,
    val targetAdvances: Long = Gen5TimerModel.Defaults.TARGET_ADVANCES,
    val calibration: Long = Gen5TimerModel.Defaults.CALIBRATION,
    val entralinkCalibration: Long = Gen5TimerModel.Defaults.ENTRALINK_CALIBRATION,
    val frameCalibration: Long = Gen5TimerModel.Defaults.FRAME_CALIBRATION
) {
    constructor(
        model: Gen5TimerModel
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
