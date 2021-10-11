//
// Created by Dylan Meadows on 2020-03-26.
//

#pragma once

#include <Util/Types.h>

#include <QObject>
#include <QSettings>

namespace EonTimer::Gen3 {
    class TimerModel : public QObject {
        Q_OBJECT
    public:
        explicit TimerModel(QSettings *settings);
        void sync(QSettings *settings) const;
        [[nodiscard]] i32 getPreTimer() const;
        [[nodiscard]] i32 getTargetFrame() const;
        [[nodiscard]] i32 getCalibration() const;
        [[nodiscard]] i32 getFrameHit() const;

    signals:
        void preTimerChanged(int newValue);
        void targetFrameChanged(int newValue);
        void calibrationChanged(int newValue);
        void frameHitChanged(int newValue);

    public slots:
        void setPreTimer(int newValue);
        void setTargetFrame(int newValue);
        void setCalibration(int newValue);
        void setFrameHit(int newValue);

    private:
        i32 preTimer;
        i32 targetFrame;
        i32 calibration;
        i32 frameHit = 0;
    };
}  // namespace EonTimer::Gen3
