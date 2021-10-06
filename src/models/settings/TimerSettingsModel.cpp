//
// Created by Dylan Meadows on 2020-03-15.
//

#include "TimerSettingsModel.h"

namespace EonTimer::settings {
    namespace TimerSettingsFields {
        const char *GROUP = "timer";
        const char *CONSOLE = "console";
        const char *REFRESH_INTERVAL = "refreshInterval";
        const char *PRECISION_CALIBRATION_ENABLED = "precisionCalibrationEnabled";

        namespace Defaults {
            const int CONSOLE = 1;
            const int REFRESH_INTERVAL = 8;
            const bool PRECISION_CALIBRATION_ENABLED = false;
        }  // namespace Defaults
    }      // namespace TimerSettingsFields

    TimerSettingsModel::TimerSettingsModel(QSettings *settings) {
        const auto &consoles = EonTimer::getConsoles();
        settings->beginGroup(TimerSettingsFields::GROUP);
        console = consoles[settings->value(TimerSettingsFields::CONSOLE, TimerSettingsFields::Defaults::CONSOLE).toInt()];
        refreshInterval = std::chrono::milliseconds(
            settings->value(TimerSettingsFields::REFRESH_INTERVAL, TimerSettingsFields::Defaults::REFRESH_INTERVAL)
                .toULongLong());
        precisionCalibrationEnabled = settings
                                          ->value(TimerSettingsFields::PRECISION_CALIBRATION_ENABLED,
                                                  TimerSettingsFields::Defaults::PRECISION_CALIBRATION_ENABLED)
                                          .toBool();
        settings->endGroup();
    }

    void TimerSettingsModel::sync(QSettings *settings) const {
        settings->beginGroup(TimerSettingsFields::GROUP);
        settings->setValue(TimerSettingsFields::CONSOLE, console);
        settings->setValue(TimerSettingsFields::REFRESH_INTERVAL, static_cast<int>(refreshInterval.count()));
        settings->setValue(TimerSettingsFields::PRECISION_CALIBRATION_ENABLED, precisionCalibrationEnabled);
        settings->endGroup();
    }

    EonTimer::Console TimerSettingsModel::getConsole() const { return console; }

    void TimerSettingsModel::setConsole(EonTimer::Console mConsole) { this->console = mConsole; }

    std::chrono::milliseconds TimerSettingsModel::getRefreshInterval() const { return refreshInterval; }

    void TimerSettingsModel::setRefreshInterval(const std::chrono::milliseconds &refreshInterval) {
        this->refreshInterval = refreshInterval;
    }

    bool TimerSettingsModel::isPrecisionCalibrationEnabled() const { return precisionCalibrationEnabled; }

    void TimerSettingsModel::setPrecisionCalibrationEnabled(const bool precisionCalibrationEnabled) {
        this->precisionCalibrationEnabled = precisionCalibrationEnabled;
    }
}  // namespace EonTimer::settings
