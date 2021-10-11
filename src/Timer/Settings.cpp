//
// Created by Dylan Meadows on 2020-03-15.
//

#include "Settings.h"

#include <Util/QSettingsProperty.h>

namespace EonTimer::Timer {
    static const QString &getGroup() {
        static const QString group = "timer";
        return group;
    }

    static QString withGroup(const QString &name) {
        static const QString groupTemplate = "%1/%2";
        return groupTemplate.arg(getGroup(), name);
    }

    static std::vector<Util::QSettingsProperty> &getProperties() {
        static std::vector<Util::QSettingsProperty> properties{
            Util::QSettingsProperty(withGroup("console"), static_cast<i32>(Console::NDS)),
            Util::QSettingsProperty(withGroup("refreshInterval"), 8),
            Util::QSettingsProperty(withGroup("precisionCalibrationEnabled"), false)};
        return properties;
    }

    enum { CONSOLE, REFRESH_INTERVAL, PRECISION_CALIBRATION_ENABLED };

    Settings::Settings(QSettings *settings) : QObject(settings), settings(settings) {}

    Console Settings::getConsole() const { return getConsoles()[getProperties()[CONSOLE].getValue(*settings).toInt()]; }

    void Settings::setConsole(const i32 newValue) { getProperties()[CONSOLE].setValue(*settings, newValue); }

    void Settings::setConsole(const Console newValue) { setConsole(static_cast<i32>(newValue)); }

    std::chrono::milliseconds Settings::getRefreshInterval() const {
        return std::chrono::milliseconds(getProperties()[REFRESH_INTERVAL].getValue(*settings).toInt());
    }

    void Settings::setRefreshInterval(const int newValue) {
        getProperties()[REFRESH_INTERVAL].setValue(*settings, newValue);
    }

    void Settings::setRefreshInterval(const std::chrono::milliseconds newValue) {
        setRefreshInterval(static_cast<i32>(newValue.count()));
    }

    bool Settings::isPrecisionCalibrationEnabled() const {
        return getProperties()[PRECISION_CALIBRATION_ENABLED].getValue(*settings).toBool();
    }

    void Settings::setPrecisionCalibrationEnabled(const bool newValue) {
        getProperties()[PRECISION_CALIBRATION_ENABLED].setValue(*settings, newValue);
    }
}  // namespace EonTimer::Timer
