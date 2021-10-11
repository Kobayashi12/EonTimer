//
// Created by Dylan Meadows on 2020-03-17.
//

#include "TimerModel.h"
#include <QtCore>
#include <Util/QSettingsProperty.h>

namespace EonTimer::Gen4 {
    static std::vector<Util::QSettingsProperty> &getProperties() {
        static std::vector<Util::QSettingsProperty> properties{Util::QSettingsProperty("gen4/calibratedDelay", 500),
                                                               Util::QSettingsProperty("gen4/calibratedSecond", 14),
                                                               Util::QSettingsProperty("gen4/targetDelay", 600),
                                                               Util::QSettingsProperty("gen4/targetSecond", 50)};
        return properties;
    }

    enum { CALIBRATED_DELAY, CALIBRATED_SECOND, TARGET_DELAY, TARGET_SECOND };

    TimerModel::TimerModel(QSettings *settings) : QObject(settings), settings(settings) {}

    i32 TimerModel::getCalibratedDelay() const { return getProperties()[CALIBRATED_DELAY].getValue(*settings).toInt(); }

    i32 TimerModel::getCalibratedSecond() const {
        return getProperties()[CALIBRATED_SECOND].getValue(*settings).toInt();
    }

    u32 TimerModel::getTargetDelay() const { return getProperties()[TARGET_DELAY].getValue(*settings).toUInt(); }

    u32 TimerModel::getTargetSecond() const { return getProperties()[TARGET_SECOND].getValue(*settings).toUInt(); }

    u32 TimerModel::getDelayHit() const { return delayHit; }

    void TimerModel::setCalibratedDelay(const i32 newValue) {
        if (getCalibratedDelay() != newValue) {
            getProperties()[CALIBRATED_DELAY].setValue(*settings, newValue);
            emit calibratedDelayChanged(newValue);
        }
    }

    void TimerModel::setCalibratedSecond(const i32 newValue) {
        if (getCalibratedSecond() != newValue) {
            getProperties()[CALIBRATED_SECOND].setValue(*settings, newValue);
            emit calibratedSecondChanged(newValue);
        }
    }

    void TimerModel::setTargetDelay(const u32 newValue) {
        if (getTargetDelay() != newValue) {
            getProperties()[TARGET_DELAY].setValue(*settings, newValue);
            emit targetDelayChanged(newValue);
        }
    }

    void TimerModel::setTargetSecond(const u32 newValue) {
        if (getTargetSecond() != newValue) {
            getProperties()[TARGET_SECOND].setValue(*settings, newValue);
            emit targetSecondChanged(newValue);
        }
    }

    void TimerModel::setDelayHit(const u32 newValue) {
        if (this->delayHit != newValue) {
            this->delayHit = newValue;
            emit delayHitChanged(newValue);
        }
    }
}  // namespace EonTimer::Gen4
