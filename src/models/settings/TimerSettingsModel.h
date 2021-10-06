//
// Created by Dylan Meadows on 2020-03-15.
//

#ifndef EONTIMER_TIMERSETTINGSMODEL_H
#define EONTIMER_TIMERSETTINGSMODEL_H

#include <models/Console.h>

#include <QSettings>
#include <chrono>

namespace EonTimer::settings {
    class TimerSettingsModel {
    private:
        EonTimer::Console console;
        std::chrono::milliseconds refreshInterval;
        bool precisionCalibrationEnabled;

    public:
        explicit TimerSettingsModel(QSettings *settings);

        void sync(QSettings *settings) const;

        EonTimer::Console getConsole() const;

        void setConsole(EonTimer::Console console);

        std::chrono::milliseconds getRefreshInterval() const;

        void setRefreshInterval(const std::chrono::milliseconds &refreshInterval);

        bool isPrecisionCalibrationEnabled() const;

        void setPrecisionCalibrationEnabled(bool precisionCalibrationEnabled);
    };
}  // namespace EonTimer::settings

#endif  // EONTIMER_TIMERSETTINGSMODEL_H
