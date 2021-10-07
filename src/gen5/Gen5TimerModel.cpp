//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerModel.h"

#include <models/Property.h>

namespace EonTimer::Gen5 {
    static const QString &getGroup() {
        static const QString group = "gen5";
        return group;
    }

    static std::vector<Property> &getProperties() {
        static std::vector<Property> properties{Property("mode", 0),
                                                Property("calibration", -95),
                                                Property("frameCalibration", 0),
                                                Property("entralinkCalibration", 256),
                                                Property("targetDelay", 1200),
                                                Property("targetSecond", 50),
                                                Property("targetAdvances", 100)};
        return properties;
    }

    enum Gen5Property { MODE, CALIBRATION, FRAME_CALIBRATION, ENTRALINK_CALIBRATION, TARGET_DELAY, TARGET_SECOND, TARGET_ADVANCES };

    Gen5TimerModel::Gen5TimerModel(QSettings *settings, QObject *parent) : QObject(parent) {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        const auto &modes = EonTimer::Gen5::getGen5TimerModes();
        mode = modes[properties[MODE].getValue(*settings).toInt()];
        calibration = properties[CALIBRATION].getValue(*settings).toInt();
        frameCalibration = properties[FRAME_CALIBRATION].getValue(*settings).toInt();
        entralinkCalibration = properties[ENTRALINK_CALIBRATION].getValue(*settings).toInt();
        targetDelay = properties[TARGET_DELAY].getValue(*settings).toInt();
        targetSecond = properties[TARGET_SECOND].getValue(*settings).toInt();
        targetAdvances = properties[TARGET_ADVANCES].getValue(*settings).toInt();
        settings->endGroup();
    }

    void Gen5TimerModel::sync(QSettings *settings) const {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        properties[MODE].setValue(*settings, mode);
        properties[CALIBRATION].setValue(*settings, calibration);
        properties[FRAME_CALIBRATION].setValue(*settings, frameCalibration);
        properties[TARGET_DELAY].setValue(*settings, targetDelay);
        properties[TARGET_SECOND].setValue(*settings, targetSecond);
        properties[TARGET_ADVANCES].setValue(*settings, targetAdvances);
        settings->endGroup();
    }

    EonTimer::Gen5::Gen5TimerMode Gen5TimerModel::getMode() const { return mode; }

    void Gen5TimerModel::setMode(const EonTimer::Gen5::Gen5TimerMode newValue) {
        if (mode != newValue) {
            mode = newValue;
            emit modeChanged(newValue);
        }
    }

    i32 Gen5TimerModel::getCalibration() const { return calibration; }

    void Gen5TimerModel::setCalibration(const i32 newValue) {
        if (calibration != newValue) {
            calibration = newValue;
            emit calibrationChanged(newValue);
        }
    }

    i32 Gen5TimerModel::getFrameCalibration() const { return frameCalibration; }

    void Gen5TimerModel::setFrameCalibration(const i32 newValue) {
        if (frameCalibration != newValue) {
            frameCalibration = newValue;
            emit frameCalibrationChanged(newValue);
        }
    }

    i32 Gen5TimerModel::getEntralinkCalibration() const { return entralinkCalibration; }

    void Gen5TimerModel::setEntralinkCalibration(const i32 newValue) {
        if (entralinkCalibration != newValue) {
            entralinkCalibration = newValue;
            emit entralinkCalibrationChanged(newValue);
        }
    }

    i32 Gen5TimerModel::getTargetDelay() const { return targetDelay; }

    void Gen5TimerModel::setTargetDelay(const i32 newValue) {
        if (targetDelay != newValue) {
            targetDelay = newValue;
            emit targetDelayChanged(newValue);
        }
    }

    i32 Gen5TimerModel::getTargetSecond() const { return targetSecond; }

    void Gen5TimerModel::setTargetSecond(const i32 newValue) {
        if (targetSecond != newValue) {
            targetSecond = newValue;
            emit targetSecondChanged(newValue);
        }
    }

    i32 Gen5TimerModel::getTargetAdvances() const { return targetAdvances; }

    void Gen5TimerModel::setTargetAdvances(const i32 newValue) {
        if (targetAdvances != newValue) {
            targetAdvances = newValue;
            emit targetAdvancesChanged(newValue);
        }
    }

    i32 Gen5TimerModel::getDelayHit() const { return delayHit; }

    void Gen5TimerModel::setDelayHit(const i32 newValue) {
        if (delayHit != newValue) {
            delayHit = newValue;
            emit delayHitChanged(newValue);
        }
    }

    i32 Gen5TimerModel::getSecondHit() const { return secondHit; }

    void Gen5TimerModel::setSecondHit(const i32 newValue) {
        if (secondHit != newValue) {
            secondHit = newValue;
            emit secondHitChanged(newValue);
        }
    }

    i32 Gen5TimerModel::getAdvancesHit() const { return advancesHit; }

    void Gen5TimerModel::setAdvancesHit(const i32 newValue) {
        if (advancesHit != newValue) {
            advancesHit = newValue;
            emit advancesHitChanged(newValue);
        }
    }
}  // namespace EonTimer::Gen5
