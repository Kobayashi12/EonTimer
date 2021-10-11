//
// Created by Dylan Meadows on 2020-03-19.
//

#pragma once

#include <Util/Types.h>

#include <QObject>
#include <QSettings>

#include "TimerMode.h"

namespace EonTimer::Gen5 {
    class TimerModel : public QObject {
        Q_OBJECT
    public:
        explicit TimerModel(QSettings *settings);
        void sync(QSettings *settings) const;
        [[nodiscard]] TimerMode getMode() const;
        [[nodiscard]] i32 getCalibration() const;
        [[nodiscard]] i32 getFrameCalibration() const;
        [[nodiscard]] i32 getEntralinkCalibration() const;
        [[nodiscard]] i32 getTargetDelay() const;
        [[nodiscard]] i32 getTargetSecond() const;
        [[nodiscard]] i32 getTargetAdvances() const;
        [[nodiscard]] i32 getDelayHit() const;
        [[nodiscard]] i32 getSecondHit() const;
        [[nodiscard]] i32 getAdvancesHit() const;

    signals:
        void modeChanged(TimerMode value);
        void calibrationChanged(int value);
        void frameCalibrationChanged(int value);
        void entralinkCalibrationChanged(int value);
        void targetDelayChanged(int value);
        void targetSecondChanged(int value);
        void targetAdvancesChanged(int value);
        void delayHitChanged(int value);
        void secondHitChanged(int value);
        void advancesHitChanged(int value);

    public slots:
        void setMode(TimerMode newValue);
        void setCalibration(int newValue);
        void setFrameCalibration(int newValue);
        void setEntralinkCalibration(int newValue);
        void setTargetDelay(int newValue);
        void setTargetSecond(int newValue);
        void setTargetAdvances(int newValue);
        void setDelayHit(int newValue);
        void setSecondHit(int newValue);
        void setAdvancesHit(int newValue);

    private:
        TimerMode mode;
        i32 calibration;
        i32 frameCalibration;
        i32 entralinkCalibration;
        i32 targetDelay;
        i32 targetSecond;
        i32 targetAdvances;
        i32 delayHit = 0;
        i32 secondHit = 0;
        i32 advancesHit = 0;
    };
}  // namespace EonTimer::Gen5
