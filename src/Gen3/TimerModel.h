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
        [[nodiscard]] std::chrono::milliseconds getPreTimer() const;
        [[nodiscard]] u32 getTargetFrame() const;
        [[nodiscard]] i32 getCalibration() const;
        [[nodiscard]] u32 getFrameHit() const;

    signals:
        void preTimerChanged(std::chrono::milliseconds newValue);
        void targetFrameChanged(u32 newValue);
        void calibrationChanged(int newValue);
        void frameHitChanged(u32 newValue);

    public slots:
        void setPreTimer(i32 newValue);
        void setTargetFrame(u32 newValue);
        void setCalibration(i32 newValue);
        void setFrameHit(u32 newValue);

    private:
        QSettings *settings;
        u32 frameHit = 0;
    };
}  // namespace EonTimer::Gen3
