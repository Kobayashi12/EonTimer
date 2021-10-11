//
// Created by Dylan Meadows on 2020-03-15.
//

#pragma once

#include <Timer/Console.h>

#include <QSettings>
#include <chrono>

namespace EonTimer::Timer {
    class Settings : public QObject {
        Q_OBJECT
    public:
        explicit Settings(QSettings *settings);
        [[nodiscard]] Console getConsole() const;
        [[nodiscard]] std::chrono::milliseconds getRefreshInterval() const;
        [[nodiscard]] bool isPrecisionCalibrationEnabled() const;

        void setConsole(Console newValue);
        void setRefreshInterval(std::chrono::milliseconds newValue);

    public slots:
        void setConsole(int newValue);
        void setRefreshInterval(int newValue);
        void setPrecisionCalibrationEnabled(bool newValue);

    private:
        QSettings *settings;
    };
}  // namespace EonTimer::Timer
