//
// Created by Dylan Meadows on 2020-03-19.
//

#ifndef EONTIMER_GEN5TIMERMODEL_H
#define EONTIMER_GEN5TIMERMODEL_H

#include <util/Types.h>
#include <models/Gen5TimerMode.h>

#include <QObject>
#include <QSettings>

namespace EonTimer {
    class Gen5TimerModel : public QObject {
        Q_OBJECT
    private:
        EonTimer::Gen5TimerMode mode;
        i32 calibration;
        i32 frameCalibration;
        i32 entralinkCalibration;
        i32 targetDelay;
        i32 targetSecond;
        i32 targetAdvances;
        i32 delayHit;
        i32 secondHit;
        i32 advancesHit;

    public:
        explicit Gen5TimerModel(QSettings *settings, QObject *parent = nullptr);

        void sync(QSettings *settings) const;

        [[nodiscard]] EonTimer::Gen5TimerMode getMode() const;

        void setMode(EonTimer::Gen5TimerMode newValue);

        [[nodiscard]] i32 getCalibration() const;

        void setCalibration(i32 newValue);

        [[nodiscard]] i32 getFrameCalibration() const;

        void setFrameCalibration(i32 newValue);

        [[nodiscard]] i32 getEntralinkCalibration() const;

        void setEntralinkCalibration(i32 newValue);

        [[nodiscard]] i32 getTargetDelay() const;

        void setTargetDelay(i32 newValue);

        [[nodiscard]] i32 getTargetSecond() const;

        void setTargetSecond(i32 newValue);

        [[nodiscard]] i32 getTargetAdvances() const;

        void setTargetAdvances(i32 newValue);

        [[nodiscard]] i32 getDelayHit() const;

        void setDelayHit(i32 newValue);

        [[nodiscard]] i32 getSecondHit() const;

        void setSecondHit(i32 newValue);

        [[nodiscard]] i32 getAdvancesHit() const;

        void setAdvancesHit(i32 newValue);

        // @formatter:off
    signals:
        void modeChanged(EonTimer::Gen5TimerMode value);
        void calibrationChanged(i32 value);
        void frameCalibrationChanged(i32 value);
        void entralinkCalibrationChanged(i32 value);
        void targetDelayChanged(i32 value);
        void targetSecondChanged(i32 value);
        void targetAdvancesChanged(i32 value);
        void delayHitChanged(i32 value);
        void secondHitChanged(i32 value);
        void advancesHitChanged(i32 value);
        // @formatter:on
    };
}  // namespace EonTimer::timer

#endif  // EONTIMER_GEN5TIMERMODEL_H
