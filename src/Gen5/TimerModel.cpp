//
// Created by Dylan Meadows on 2020-03-19.
//

#include "TimerModel.h"

#include <Util/QSettingsProperty.h>

namespace EonTimer::Gen5 {
    static const QString &getGroup() {
        static const QString group = "gen5";
        return group;
    }

    static std::vector<Util::QSettingsProperty> &getProperties() {
        static std::vector<Util::QSettingsProperty> properties{
            Util::QSettingsProperty("mode", static_cast<i32>(STANDARD)),
            Util::QSettingsProperty("calibration", -95),
            Util::QSettingsProperty("frameCalibration", 0),
            Util::QSettingsProperty("entralinkCalibration", 256),
            Util::QSettingsProperty("targetDelay", 1200),
            Util::QSettingsProperty("targetSecond", 50),
            Util::QSettingsProperty("targetAdvances", 100)};
        return properties;
    }

    enum { MODE, CALIBRATION, FRAME_CALIBRATION, ENTRALINK_CALIBRATION, TARGET_DELAY, TARGET_SECOND, TARGET_ADVANCES };

    TimerModel::TimerModel(QSettings *settings) : QObject(settings) {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        mode = getGen5TimerModes()[properties[MODE].getValue(*settings).toInt()];
        calibration = properties[CALIBRATION].getValue(*settings).toInt();
        frameCalibration = properties[FRAME_CALIBRATION].getValue(*settings).toInt();
        entralinkCalibration = properties[ENTRALINK_CALIBRATION].getValue(*settings).toInt();
        targetDelay = properties[TARGET_DELAY].getValue(*settings).toInt();
        targetSecond = properties[TARGET_SECOND].getValue(*settings).toInt();
        targetAdvances = properties[TARGET_ADVANCES].getValue(*settings).toInt();
        settings->endGroup();
    }

    void TimerModel::sync(QSettings *settings) const {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        properties[MODE].setValue(*settings, static_cast<i32>(mode));
        properties[CALIBRATION].setValue(*settings, calibration);
        properties[FRAME_CALIBRATION].setValue(*settings, frameCalibration);
        properties[ENTRALINK_CALIBRATION].setValue(*settings, entralinkCalibration);
        properties[TARGET_DELAY].setValue(*settings, targetDelay);
        properties[TARGET_SECOND].setValue(*settings, targetSecond);
        properties[TARGET_ADVANCES].setValue(*settings, targetAdvances);
        settings->endGroup();
    }

    TimerMode TimerModel::getMode() const { return mode; }

    i32 TimerModel::getCalibration() const { return calibration; }

    i32 TimerModel::getFrameCalibration() const { return frameCalibration; }

    i32 TimerModel::getEntralinkCalibration() const { return entralinkCalibration; }

    i32 TimerModel::getTargetDelay() const { return targetDelay; }

    i32 TimerModel::getTargetSecond() const { return targetSecond; }

    i32 TimerModel::getTargetAdvances() const { return targetAdvances; }

    i32 TimerModel::getDelayHit() const { return delayHit; }

    i32 TimerModel::getSecondHit() const { return secondHit; }

    i32 TimerModel::getAdvancesHit() const { return advancesHit; }

    void TimerModel::setMode(const TimerMode newValue) {
        if (mode != newValue) {
            mode = newValue;
            emit modeChanged(newValue);
        }
    }

    void TimerModel::setCalibration(const i32 newValue) {
        if (calibration != newValue) {
            calibration = newValue;
            emit calibrationChanged(newValue);
        }
    }

    void TimerModel::setFrameCalibration(const i32 newValue) {
        if (frameCalibration != newValue) {
            frameCalibration = newValue;
            emit frameCalibrationChanged(newValue);
        }
    }

    void TimerModel::setEntralinkCalibration(const i32 newValue) {
        if (entralinkCalibration != newValue) {
            entralinkCalibration = newValue;
            emit entralinkCalibrationChanged(newValue);
        }
    }

    void TimerModel::setTargetDelay(const i32 newValue) {
        if (targetDelay != newValue) {
            targetDelay = newValue;
            emit targetDelayChanged(newValue);
        }
    }

    void TimerModel::setTargetSecond(const i32 newValue) {
        if (targetSecond != newValue) {
            targetSecond = newValue;
            emit targetSecondChanged(newValue);
        }
    }

    void TimerModel::setTargetAdvances(const i32 newValue) {
        if (targetAdvances != newValue) {
            targetAdvances = newValue;
            emit targetAdvancesChanged(newValue);
        }
    }

    void TimerModel::setDelayHit(const i32 newValue) {
        if (delayHit != newValue) {
            delayHit = newValue;
            emit delayHitChanged(newValue);
        }
    }

    void TimerModel::setSecondHit(const i32 newValue) {
        if (secondHit != newValue) {
            secondHit = newValue;
            emit secondHitChanged(newValue);
        }
    }

    void TimerModel::setAdvancesHit(const i32 newValue) {
        if (advancesHit != newValue) {
            advancesHit = newValue;
            emit advancesHitChanged(newValue);
        }
    }
}  // namespace EonTimer::Gen5
