//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerModel.h"

namespace EonTimer {

    namespace Gen5Fields {
        const char *GROUP = "gen5";
        const char *MODE = "mode";
        const char *CALIBRATION = "calibration";
        const char *FRAME_CALIBRATION = "frameCalibration";
        const char *ENTRALINK_CALIBRATION = "entralinkCalibration";
        const char *TARGET_DELAY = "targetDelay";
        const char *TARGET_SECOND = "targetSecond";
        const char *TARGET_ADVANCES = "targetAdvances";

        namespace Defaults {
            const i32 MODE = 0;
            const i32 CALIBRATION = -95;
            const i32 FRAME_CALIBRATION = 0;
            const i32 ENTRALINK_CALIBRATION = 256;
            const i32 TARGET_DELAY = 1200;
            const i32 TARGET_SECOND = 50;
            const i32 TARGET_ADVANCES = 100;
        }  // namespace Defaults
    }      // namespace Gen5Fields

    Gen5TimerModel::Gen5TimerModel(QSettings *settings, QObject *parent) : QObject(parent) {
        settings->beginGroup(Gen5Fields::GROUP);
        const auto &modes = EonTimer::getGen5TimerModes();
        mode = modes[settings->value(Gen5Fields::MODE, Gen5Fields::Defaults::MODE).toInt()];
        calibration = settings->value(Gen5Fields::CALIBRATION, Gen5Fields::Defaults::CALIBRATION).toInt();
        frameCalibration =
            settings->value(Gen5Fields::FRAME_CALIBRATION, Gen5Fields::Defaults::FRAME_CALIBRATION).toInt();
        entralinkCalibration =
            settings->value(Gen5Fields::ENTRALINK_CALIBRATION, Gen5Fields::Defaults::ENTRALINK_CALIBRATION).toInt();
        targetDelay = settings->value(Gen5Fields::TARGET_DELAY, Gen5Fields::Defaults::TARGET_DELAY).toInt();
        targetSecond = settings->value(Gen5Fields::TARGET_SECOND, Gen5Fields::Defaults::TARGET_SECOND).toInt();
        targetAdvances = settings->value(Gen5Fields::TARGET_ADVANCES, Gen5Fields::Defaults::TARGET_ADVANCES).toInt();
        settings->endGroup();
    }

    void Gen5TimerModel::sync(QSettings *settings) const {
        settings->beginGroup(Gen5Fields::GROUP);
        settings->setValue(Gen5Fields::MODE, mode);
        settings->setValue(Gen5Fields::CALIBRATION, calibration);
        settings->setValue(Gen5Fields::FRAME_CALIBRATION, frameCalibration);
        settings->setValue(Gen5Fields::ENTRALINK_CALIBRATION, entralinkCalibration);
        settings->setValue(Gen5Fields::TARGET_DELAY, targetDelay);
        settings->setValue(Gen5Fields::TARGET_SECOND, targetSecond);
        settings->setValue(Gen5Fields::TARGET_ADVANCES, targetAdvances);
        settings->endGroup();
    }

    EonTimer::Gen5TimerMode Gen5TimerModel::getMode() const { return mode; }

    void Gen5TimerModel::setMode(const EonTimer::Gen5TimerMode newValue) {
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
}  // namespace EonTimer::timer
