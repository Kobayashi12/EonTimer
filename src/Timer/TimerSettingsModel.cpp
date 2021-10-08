//
// Created by Dylan Meadows on 2020-03-15.
//

#include "TimerSettingsModel.h"

#include <Util/QSettingsProperty.h>

namespace EonTimer::Timer {
    static const QString &getGroup() {
        static const QString group = "timer";
        return group;
    }

    static std::vector<Util::QSettingsProperty> &getProperties() {
        static std::vector<Util::QSettingsProperty> properties{
            Util::QSettingsProperty("console", static_cast<i32>(Console::NDS)),
            Util::QSettingsProperty("refreshInterval", 8),
            Util::QSettingsProperty("precisionCalibrationEnabled", false)};
        return properties;
    }

    enum { CONSOLE, REFRESH_INTERVAL, PRECISION_CALIBRATION_ENABLED };

    TimerSettingsModel::TimerSettingsModel(QSettings *settings, QObject *parent) : QObject(parent) {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
//        console = getConsoles()[properties[CONSOLE].getValue(*settings).toInt()];
        console = Console::NDS;
        refreshInterval = std::chrono::milliseconds(properties[REFRESH_INTERVAL].getValue(*settings).toULongLong());
        precisionCalibrationEnabled = properties[PRECISION_CALIBRATION_ENABLED].getValue(*settings).toBool();
        settings->endGroup();
    }

    void TimerSettingsModel::sync(QSettings *settings) const {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        properties[CONSOLE].setValue(*settings, static_cast<i32>(console));
        properties[REFRESH_INTERVAL].setValue(*settings, refreshInterval.count());
        properties[PRECISION_CALIBRATION_ENABLED].setValue(*settings, precisionCalibrationEnabled);
        settings->endGroup();
    }

    Console TimerSettingsModel::getConsole() const { return console; }

    std::chrono::milliseconds TimerSettingsModel::getRefreshInterval() const { return refreshInterval; }

    bool TimerSettingsModel::isPrecisionCalibrationEnabled() const { return precisionCalibrationEnabled; }

    void TimerSettingsModel::setConsole(const Console newValue) { this->console = newValue; }

    void TimerSettingsModel::setRefreshInterval(const std::chrono::milliseconds newValue) {
        this->refreshInterval = newValue;
    }

    void TimerSettingsModel::setPrecisionCalibrationEnabled(const bool newValue) {
        this->precisionCalibrationEnabled = newValue;
    }
}  // namespace EonTimer::Timer
