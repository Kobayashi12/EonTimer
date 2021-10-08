//
// Created by Dylan Meadows on 2020-03-15.
//

#pragma once

#include <Timer/Console.h>

#include <QSettings>
#include <chrono>

namespace EonTimer::Timer {
    class TimerSettingsModel : public QObject {
        Q_OBJECT
    public:
        explicit TimerSettingsModel(QSettings *settings, QObject *parent = nullptr);
        void sync(QSettings *settings) const;
        [[nodiscard]] Console getConsole() const;
        [[nodiscard]] std::chrono::milliseconds getRefreshInterval() const;
        [[nodiscard]] bool isPrecisionCalibrationEnabled() const;

    public slots:
        void setConsole(EonTimer::Timer::Console newValue);
        void setRefreshInterval(std::chrono::milliseconds newValue);
        void setPrecisionCalibrationEnabled(bool newValue);

    private:
        Console console;
        std::chrono::milliseconds refreshInterval;
        bool precisionCalibrationEnabled;
    };
}  // namespace EonTimer::Timer
