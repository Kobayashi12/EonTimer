package io.eontimer.timer

data class StoredSettings(
    val console: Console = Defaults.CONSOLE,
    val refreshInterval: Long = Defaults.REFRESH_INTERVAL,
    val precisionCalibrationMode: Boolean = Defaults.PRECISION_CALIBRATION_MODE
) {
    constructor(
        settings: Settings
    ) : this(
        console = settings.console.get(),
        refreshInterval = settings.refreshInterval.get(),
        precisionCalibrationMode = settings.precisionCalibrationMode.get()
    )
}
