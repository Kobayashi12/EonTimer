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

    enum Gen3Property { PRE_TIMER, TARGET_FRAME, CALIBRATION, COUNT };

    static std::vector<Property> &getProperties() {
        static std::vector<Property> properties{Property("preTimer", 5000),
                                                Property("targetFrame", 1000),
                                                Property("calibration", 0)};
        return properties;
    }

    Timer::Timer(QSettings *settings, QObject *parent) : QObject(parent) {
        settings->beginGroup(Gen3::getGroup());
        auto &properties = Gen3::getProperties();
        preTimer = properties[Gen3::PRE_TIMER].getValue(*settings).toInt();
        targetFrame = properties[Gen3::TARGET_FRAME].getValue(*settings).toInt();
        calibration = properties[Gen3::CALIBRATION].getValue(*settings).toInt();
        settings->endGroup();
    }

    void Timer::sync(QSettings *settings) const {
        settings->beginGroup(Gen3::getGroup());
        auto &properties = Gen3::getProperties();
        properties[Gen3::PRE_TIMER].setValue(*settings, preTimer);
        properties[Gen3::TARGET_FRAME].setValue(*settings, targetFrame);
        properties[Gen3::CALIBRATION].setValue(*settings, calibration);
        settings->endGroup();
    }

    i32 Timer::getPreTimer() const { return preTimer; }

    void Timer::setPreTimer(const i32 newValue) {
        if (preTimer != newValue) {
            preTimer = newValue;
            emit preTimerChanged(newValue);
        }
    }

    i32 Timer::getTargetFrame() const { return targetFrame; }

    void Timer::setTargetFrame(const i32 newValue) {
        if (targetFrame != newValue) {
            targetFrame = newValue;
            emit targetFrameChanged(newValue);
        }
    }

    i32 Timer::getCalibration() const { return calibration; }

    void Timer::setCalibration(const i32 newValue) {
        if (calibration != newValue) {
            calibration = newValue;
            emit calibrationChanged(newValue);
        }
    }

    i32 Timer::getFrameHit() const { return frameHit; }

    void Timer::setFrameHit(const i32 newValue) {
        if (frameHit != newValue) {
            frameHit = newValue;
            emit frameHitChanged(newValue);
        }
    }
}  // namespace EonTimer::Gen3
