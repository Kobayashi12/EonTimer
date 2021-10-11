//
// Created by Dylan Meadows on 2020-03-26.
//

#include "TimerModel.h"

#include <Util/QSettingsProperty.h>

namespace EonTimer::Gen3 {
    static const QString &getGroup() {
        static const QString group = "gen3";
        return group;
    }

    static std::vector<Util::QSettingsProperty> &getProperties() {
        static std::vector<Util::QSettingsProperty> properties{Util::QSettingsProperty("preTimer", 5000),
                                                               Util::QSettingsProperty("targetFrame", 1000),
                                                               Util::QSettingsProperty("calibration", 0)};
        return properties;
    }

    enum { PRE_TIMER, TARGET_FRAME, CALIBRATION };

    TimerModel::TimerModel(QSettings *settings) : QObject(settings) {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        preTimer = properties[PRE_TIMER].getValue(*settings).toInt();
        targetFrame = properties[TARGET_FRAME].getValue(*settings).toInt();
        calibration = properties[CALIBRATION].getValue(*settings).toInt();
        settings->endGroup();
    }

    void TimerModel::sync(QSettings *settings) const {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        properties[PRE_TIMER].setValue(*settings, preTimer);
        properties[TARGET_FRAME].setValue(*settings, targetFrame);
        properties[CALIBRATION].setValue(*settings, calibration);
        settings->endGroup();
    }

    i32 TimerModel::getPreTimer() const { return preTimer; }

    i32 TimerModel::getTargetFrame() const { return targetFrame; }

    i32 TimerModel::getCalibration() const { return calibration; }

    i32 TimerModel::getFrameHit() const { return frameHit; }

    void TimerModel::setPreTimer(const i32 newValue) {
        if (preTimer != newValue) {
            preTimer = newValue;
            emit preTimerChanged(newValue);
        }
    }

    void TimerModel::setTargetFrame(const i32 newValue) {
        if (targetFrame != newValue) {
            targetFrame = newValue;
            emit targetFrameChanged(newValue);
        }
    }

    void TimerModel::setCalibration(const i32 newValue) {
        if (calibration != newValue) {
            calibration = newValue;
            emit calibrationChanged(newValue);
        }
    }

    void TimerModel::setFrameHit(const i32 newValue) {
        if (frameHit != newValue) {
            frameHit = newValue;
            emit frameHitChanged(newValue);
        }
    }
}  // namespace EonTimer::Gen3
