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
        [[nodiscard]] TimerMode getMode() const;
        [[nodiscard]] i32 getCalibration() const;
        [[nodiscard]] i32 getFrameCalibration() const;
        [[nodiscard]] i32 getEntralinkCalibration() const;
        [[nodiscard]] u32 getTargetDelay() const;
        [[nodiscard]] u32 getTargetSecond() const;
        [[nodiscard]] u32 getTargetAdvances() const;
        [[nodiscard]] u32 getDelayHit() const;
        [[nodiscard]] u32 getSecondHit() const;
        [[nodiscard]] u32 getAdvancesHit() const;

    signals:
        void modeChanged(EonTimer::Gen5::TimerMode value);
        void calibrationChanged(i32 value);
        void frameCalibrationChanged(i32 value);
        void entralinkCalibrationChanged(i32 value);
        void targetDelayChanged(u32 value);
        void targetSecondChanged(u32 value);
        void targetAdvancesChanged(u32 value);
        void delayHitChanged(u32 value);
        void secondHitChanged(u32 value);
        void advancesHitChanged(u32 value);

    public slots:
        void setMode(TimerMode newValue);
        void setCalibration(i32 newValue);
        void setFrameCalibration(i32 newValue);
        void setEntralinkCalibration(i32 newValue);
        void setTargetDelay(u32 newValue);
        void setTargetSecond(u32 newValue);
        void setTargetAdvances(u32 newValue);
        void setDelayHit(u32 newValue);
        void setSecondHit(u32 newValue);
        void setAdvancesHit(u32 newValue);

    private:
        QSettings *settings;
        u32 delayHit = 0;
        u32 secondHit = 0;
        u32 advancesHit = 0;
    };
}  // namespace EonTimer::Gen5
