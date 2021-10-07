//
// Created by Dylan Meadows on 2020-03-26.
//

#include "Gen3TimerModel.h"

#include <models/Property.h>

namespace EonTimer::Gen3 {
    static const QString &getGroup() {
        static const QString group = "gen3";
        return group;
    }

    static std::vector<Property> &getProperties() {
        static std::vector<Property> properties{Property("preTimer", 5000),
                                                Property("targetFrame", 1000),
                                                Property("calibration", 0)};
        return properties;
    }

    enum Gen3Property { PRE_TIMER, TARGET_FRAME, CALIBRATION };

    Gen3TimerModel::Gen3TimerModel(QSettings *settings, QObject *parent) : QObject(parent) {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        preTimer = properties[PRE_TIMER].getValue(*settings).toInt();
        targetFrame = properties[TARGET_FRAME].getValue(*settings).toInt();
        calibration = properties[CALIBRATION].getValue(*settings).toInt();
        settings->endGroup();
    }

    void Gen3TimerModel::sync(QSettings *settings) const {
        settings->beginGroup(Gen3::getGroup());
        auto &properties = Gen3::getProperties();
        properties[PRE_TIMER].setValue(*settings, preTimer);
        properties[TARGET_FRAME].setValue(*settings, targetFrame);
        properties[CALIBRATION].setValue(*settings, calibration);
        settings->endGroup();
    }

    i32 Gen3TimerModel::getPreTimer() const { return preTimer; }

    void Gen3TimerModel::setPreTimer(const i32 newValue) {
        if (preTimer != newValue) {
            preTimer = newValue;
            emit preTimerChanged(newValue);
        }
    }

    i32 Gen3TimerModel::getTargetFrame() const { return targetFrame; }

    void Gen3TimerModel::setTargetFrame(const i32 newValue) {
        if (targetFrame != newValue) {
            targetFrame = newValue;
            emit targetFrameChanged(newValue);
        }
    }

    i32 Gen3TimerModel::getCalibration() const { return calibration; }

    void Gen3TimerModel::setCalibration(const i32 newValue) {
        if (calibration != newValue) {
            calibration = newValue;
            emit calibrationChanged(newValue);
        }
    }

    i32 Gen3TimerModel::getFrameHit() const { return frameHit; }

    void Gen3TimerModel::setFrameHit(const i32 newValue) {
        if (frameHit != newValue) {
            frameHit = newValue;
            emit frameHitChanged(newValue);
        }
    }
}  // namespace EonTimer::Gen3
