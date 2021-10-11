//
// Created by Dylan Meadows on 2020-03-19.
//

#include "TimerModel.h"

#include <Util/QSettingsProperty.h>

namespace EonTimer::Gen5 {
    static std::vector<Util::QSettingsProperty> &getProperties() {
        static std::vector<Util::QSettingsProperty> properties{
            Util::QSettingsProperty("gen5/mode", static_cast<i32>(STANDARD)),
            Util::QSettingsProperty("gen5/calibration", -95),
            Util::QSettingsProperty("gen5/frameCalibration", 0),
            Util::QSettingsProperty("gen5/entralinkCalibration", 256),
            Util::QSettingsProperty("gen5/targetDelay", 1200),
            Util::QSettingsProperty("gen5/targetSecond", 50),
            Util::QSettingsProperty("gen5/targetAdvances", 100)};
        return properties;
    }

    enum { MODE, CALIBRATION, FRAME_CALIBRATION, ENTRALINK_CALIBRATION, TARGET_DELAY, TARGET_SECOND, TARGET_ADVANCES };

    TimerModel::TimerModel(QSettings *settings) : QObject(settings), settings(settings) {
//        settings->beginGroup(getGroup());
//        auto &properties = getProperties();
//        mode = getGen5TimerModes()[properties[MODE].getValue(*settings).toInt()];
//        calibration = properties[CALIBRATION].getValue(*settings).toInt();
//        frameCalibration = properties[FRAME_CALIBRATION].getValue(*settings).toInt();
//        entralinkCalibration = properties[ENTRALINK_CALIBRATION].getValue(*settings).toInt();
//        targetDelay = properties[TARGET_DELAY].getValue(*settings).toInt();
//        targetSecond = properties[TARGET_SECOND].getValue(*settings).toInt();
//        targetAdvances = properties[TARGET_ADVANCES].getValue(*settings).toInt();
//        settings->endGroup();
    }

//    void TimerModel::sync(QSettings *settings) const {
//        settings->beginGroup(getGroup());
//        auto &properties = getProperties();
//        properties[MODE].setValue(*settings, static_cast<i32>(mode));
//        properties[CALIBRATION].setValue(*settings, calibration);
//        properties[FRAME_CALIBRATION].setValue(*settings, frameCalibration);
//        properties[ENTRALINK_CALIBRATION].setValue(*settings, entralinkCalibration);
//        properties[TARGET_DELAY].setValue(*settings, targetDelay);
//        properties[TARGET_SECOND].setValue(*settings, targetSecond);
//        properties[TARGET_ADVANCES].setValue(*settings, targetAdvances);
//        settings->endGroup();
//    }

    TimerMode TimerModel::getMode() const {
        return static_cast<TimerMode>(getProperties()[MODE].getValue(*settings).toUInt());
    }

    i32 TimerModel::getCalibration() const {
        return getProperties()[CALIBRATION].getValue(*settings).toInt();
    }

    i32 TimerModel::getFrameCalibration() const {
        return getProperties()[FRAME_CALIBRATION].getValue(*settings).toInt();
    }

    i32 TimerModel::getEntralinkCalibration() const {
        return getProperties()[ENTRALINK_CALIBRATION].getValue(*settings).toInt();
    }

    u32 TimerModel::getTargetDelay() const {
        return getProperties()[TARGET_DELAY].getValue(*settings).toUInt();
    }

    u32 TimerModel::getTargetSecond() const {
        return getProperties()[TARGET_SECOND].getValue(*settings).toUInt();
    }

    u32 TimerModel::getTargetAdvances() const {
        return getProperties()[TARGET_ADVANCES].getValue(*settings).toUInt();
    }

    u32 TimerModel::getDelayHit() const { return delayHit; }

    u32 TimerModel::getSecondHit() const { return secondHit; }

    u32 TimerModel::getAdvancesHit() const { return advancesHit; }

    void TimerModel::setMode(const TimerMode newValue) {
        if (getMode() != newValue) {
            getProperties()[MODE].setValue(*settings, static_cast<u32>(newValue));
            emit modeChanged(newValue);
        }
    }

    void TimerModel::setCalibration(const i32 newValue) {
        if (getCalibration() != newValue) {
            getProperties()[CALIBRATION].setValue(*settings, newValue);
            emit calibrationChanged(newValue);
        }
    }

    void TimerModel::setFrameCalibration(const i32 newValue) {
        if (getFrameCalibration() != newValue) {
            getProperties()[FRAME_CALIBRATION].setValue(*settings, newValue);
            emit frameCalibrationChanged(newValue);
        }
    }

    void TimerModel::setEntralinkCalibration(const i32 newValue) {
        if (getEntralinkCalibration() != newValue) {
            getProperties()[ENTRALINK_CALIBRATION].setValue(*settings, newValue);
            emit entralinkCalibrationChanged(newValue);
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

    void TimerModel::setTargetAdvances(const u32 newValue) {
        if (getTargetAdvances() != newValue) {
            getProperties()[TARGET_ADVANCES].setValue(*settings, newValue);
            emit targetAdvancesChanged(newValue);
        }
    }

    void TimerModel::setDelayHit(const u32 newValue) {
        if (delayHit != newValue) {
            delayHit = newValue;
            emit delayHitChanged(newValue);
        }
    }

    void TimerModel::setSecondHit(const u32 newValue) {
        if (secondHit != newValue) {
            secondHit = newValue;
            emit secondHitChanged(newValue);
        }
    }

    void TimerModel::setAdvancesHit(const u32 newValue) {
        if (advancesHit != newValue) {
            advancesHit = newValue;
            emit advancesHitChanged(newValue);
        }
    }
}  // namespace EonTimer::Gen5
