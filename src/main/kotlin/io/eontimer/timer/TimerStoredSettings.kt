package io.eontimer.timer

data class TimerStoredSettings(
    val console: Console = TimerSettings.Defaults.CONSOLE,
    val refreshInterval: Long = TimerSettings.Defaults.REFRESH_INTERVAL,
    val precisionCalibrationMode: Boolean = TimerSettings.Defaults.PRECISION_CALIBRATION_MODE
) {
    constructor(
        settings: TimerSettings
    ) : this(
        console = settings.console.get(),
        refreshInterval = settings.refreshInterval.get(),
        precisionCalibrationMode = settings.precisionCalibration.get()
    )
}
