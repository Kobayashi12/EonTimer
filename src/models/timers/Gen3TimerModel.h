//
// Created by Dylan Meadows on 2020-03-26.
//

#ifndef EONTIMER_GEN3TIMERMODEL_H
#define EONTIMER_GEN3TIMERMODEL_H

#include <util/Types.h>
#include <QObject>
#include <QSettings>

namespace EonTimer::Gen3 {
    class Timer : public QObject {
        Q_OBJECT
    private:
        i32 preTimer;
        i32 targetFrame;
        i32 calibration;
        i32 frameHit;

    public:
        explicit Timer(QSettings *settings, QObject *parent = nullptr);

        void sync(QSettings *settings) const;

        [[nodiscard]] i32 getPreTimer() const;

        void setPreTimer(i32 newValue);

        [[nodiscard]] i32 getTargetFrame() const;

        void setTargetFrame(i32 newValue);

        [[nodiscard]] i32 getCalibration() const;

        void setCalibration(i32 newValue);

        [[nodiscard]] i32 getFrameHit() const;

        void setFrameHit(i32 newValue);

        // @formatter:off
    signals:
        void preTimerChanged(i32 value);
        void targetFrameChanged(i32 value);
        void calibrationChanged(i32 value);
        void frameHitChanged(i32 value);
        // @formatter:on
    };
}  // namespace EonTimer::timer

#endif  // EONTIMER_GEN3TIMERMODEL_H
