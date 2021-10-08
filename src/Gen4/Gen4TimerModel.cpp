//
// Created by Dylan Meadows on 2020-03-17.
//

#include "Gen4TimerModel.h"

#include <Util/QSettingsProperty.h>

#include <QtCore>

namespace EonTimer::Gen4 {
    static const QString &getGroup() {
        static const QString group = "gen4";
        return group;
    }

    static std::vector<Util::QSettingsProperty> &getProperties() {
        static std::vector<Util::QSettingsProperty> properties{Util::QSettingsProperty("calibratedDelay", 500),
                                                               Util::QSettingsProperty("calibratedSecond", 14),
                                                               Util::QSettingsProperty("targetDelay", 600),
                                                               Util::QSettingsProperty("targetSecond", 50)};
        return properties;
    }

    enum { CALIBRATED_DELAY, CALIBRATED_SECOND, TARGET_DELAY, TARGET_SECOND };

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

    i32 Gen4TimerModel::getCalibratedSecond() const { return calibratedSecond; }

    i32 Gen4TimerModel::getTargetDelay() const { return targetDelay; }

    i32 Gen4TimerModel::getTargetSecond() const { return targetSecond; }

    i32 Gen4TimerModel::getDelayHit() const { return delayHit; }

    void Gen4TimerModel::setCalibratedDelay(const i32 newValue) {
        if (this->calibratedDelay != newValue) {
            this->calibratedDelay = newValue;
            emit calibratedDelayChanged(newValue);
        }
    }

    void Gen4TimerModel::setCalibratedSecond(const i32 newValue) {
        if (this->calibratedSecond != newValue) {
            this->calibratedSecond = newValue;
            emit calibratedSecondChanged(newValue);
        }
    }

    void Gen4TimerModel::setTargetDelay(const i32 newValue) {
        if (this->targetDelay != newValue) {
            this->targetDelay = newValue;
            emit targetDelayChanged(newValue);
        }
    }

    void Gen4TimerModel::setTargetSecond(const i32 newValue) {
        if (this->targetSecond != newValue) {
            this->targetSecond = newValue;
            emit targetSecondChanged(newValue);
        }
    }

    void Gen4TimerModel::setDelayHit(const i32 newValue) {
        if (this->delayHit != newValue) {
            this->delayHit = newValue;
            emit delayHitChanged(newValue);
        }
    }
}  // namespace EonTimer::Gen4
