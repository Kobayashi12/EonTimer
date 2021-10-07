//
// Created by Dylan Meadows on 2020-03-17.
//

#include "Gen4TimerModel.h"

#include <models/Property.h>

#include <QtCore>

namespace EonTimer::Gen4 {
    static const QString &getGroup() {
        static const QString group = "gen4";
        return group;
    }

    static std::vector<Property> &getProperties() {
        static std::vector<Property> properties{Property("calibratedDelay", 500),
                                                Property("calibratedSecond", 14),
                                                Property("targetDelay", 600),
                                                Property("targetSecond", 50)};
        return properties;
    }

    enum Gen4Property { CALIBRATED_DELAY, CALIBRATED_SECOND, TARGET_DELAY, TARGET_SECOND };

    Gen4TimerModel::Gen4TimerModel(QSettings *settings, QObject *parent) : QObject(parent) {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        calibratedDelay = properties[CALIBRATED_DELAY].getValue(*settings).toInt();
        calibratedSecond = properties[CALIBRATED_SECOND].getValue(*settings).toInt();
        targetDelay = properties[TARGET_DELAY].getValue(*settings).toInt();
        targetSecond = properties[TARGET_SECOND].getValue(*settings).toInt();
        settings->endGroup();
    }

    void Gen4TimerModel::sync(QSettings *settings) const {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        properties[CALIBRATED_DELAY].setValue(*settings, calibratedDelay);
        properties[CALIBRATED_SECOND].setValue(*settings, calibratedSecond);
        properties[TARGET_DELAY].setValue(*settings, targetDelay);
        properties[TARGET_SECOND].setValue(*settings, targetSecond);
        settings->endGroup();
    }

    i32 Gen4TimerModel::getCalibratedDelay() const { return calibratedDelay; }

    void Gen4TimerModel::setCalibratedDelay(const i32 newValue) {
        if (this->calibratedDelay != newValue) {
            this->calibratedDelay = newValue;
            emit calibratedDelayChanged(newValue);
        }
    }

    i32 Gen4TimerModel::getCalibratedSecond() const { return calibratedSecond; }

    void Gen4TimerModel::setCalibratedSecond(const i32 newValue) {
        if (this->calibratedSecond != newValue) {
            this->calibratedSecond = newValue;
            emit calibratedSecondChanged(newValue);
        }
    }

    i32 Gen4TimerModel::getTargetDelay() const { return targetDelay; }

    void Gen4TimerModel::setTargetDelay(const i32 newValue) {
        if (this->targetDelay != newValue) {
            this->targetDelay = newValue;
            emit targetDelayChanged(newValue);
        }
    }

    i32 Gen4TimerModel::getTargetSecond() const { return targetSecond; }

    void Gen4TimerModel::setTargetSecond(const i32 newValue) {
        if (this->targetSecond != newValue) {
            this->targetSecond = newValue;
            emit targetSecondChanged(newValue);
        }
    }

    i32 Gen4TimerModel::getDelayHit() const { return delayHit; }

    void Gen4TimerModel::setDelayHit(const i32 newValue) {
        if (this->delayHit != newValue) {
            this->delayHit = newValue;
            emit delayHitChanged(newValue);
        }
    }
}  // namespace EonTimer::Gen4
