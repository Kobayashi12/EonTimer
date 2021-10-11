//
// Created by Dylan Meadows on 2020-03-26.
//

#include "TimerModel.h"
#include <Util/QSettingsProperty.h>

namespace EonTimer::Gen3 {
    static std::vector<Util::QSettingsProperty> &getProperties() {
        static std::vector<Util::QSettingsProperty> properties{Util::QSettingsProperty("gen3/preTimer", 5000),
                                                               Util::QSettingsProperty("gen3/targetFrame", 1000),
                                                               Util::QSettingsProperty("gen3/calibration", 0)};
        return properties;
    }

    enum { PRE_TIMER, TARGET_FRAME, CALIBRATION };

    TimerModel::TimerModel(QSettings *settings) : QObject(settings), settings(settings) {}

    std::chrono::milliseconds TimerModel::getPreTimer() const {
        return std::chrono::milliseconds(getProperties()[PRE_TIMER].getValue(*settings).toUInt());
    }

    u32 TimerModel::getTargetFrame() const { return getProperties()[TARGET_FRAME].getValue(*settings).toUInt(); }

    i32 TimerModel::getCalibration() const { return getProperties()[CALIBRATION].getValue(*settings).toInt(); }

    u32 TimerModel::getFrameHit() const { return frameHit; }

    void TimerModel::setPreTimer(const i32 newValue) {
        const auto preTimer = getPreTimer();
        if (preTimer.count() != newValue) {
            getProperties()[PRE_TIMER].setValue(*settings, newValue);
            emit preTimerChanged(std::chrono::milliseconds(newValue));
        }
    }

    void TimerModel::setTargetFrame(const u32 newValue) {
        const auto targetFrame = getTargetFrame();
        if (targetFrame != newValue) {
            getProperties()[TARGET_FRAME].setValue(*settings, newValue);
            emit targetFrameChanged(newValue);
        }
    }

    void TimerModel::setCalibration(const i32 newValue) {
        const auto calibration = getCalibration();
        if (calibration != newValue) {
            getProperties()[CALIBRATION].setValue(*settings, newValue);
            emit calibrationChanged(newValue);
        }
    }

    void TimerModel::setFrameHit(const u32 newValue) {
        if (frameHit != newValue) {
            frameHit = newValue;
            emit frameHitChanged(newValue);
        }
    }
}  // namespace EonTimer::Gen3
