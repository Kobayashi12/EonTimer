//
// Created by Dylan Meadows on 2020-03-17.
//

#pragma once

#include <Util/Types.h>

#include <QObject>
#include <QSettings>

namespace EonTimer::Gen4 {
    class TimerModel : public QObject {
        Q_OBJECT
    public:
        explicit TimerModel(QSettings *settings);
        [[nodiscard]] i32 getCalibratedDelay() const;
        [[nodiscard]] i32 getCalibratedSecond() const;
        [[nodiscard]] u32 getTargetDelay() const;
        [[nodiscard]] u32 getTargetSecond() const;
        [[nodiscard]] u32 getDelayHit() const;

    signals:
        void calibratedDelayChanged(i32 calibratedDelay);
        void calibratedSecondChanged(i32 calibratedSecond);
        void targetDelayChanged(u32 targetDelay);
        void targetSecondChanged(u32 targetSecond);
        void delayHitChanged(u32 delayHit);

    public slots:
        void setCalibratedDelay(i32 newValue);
        void setCalibratedSecond(i32 newValue);
        void setTargetDelay(u32 newValue);
        void setTargetSecond(u32 newValue);
        void setDelayHit(u32 newValue);

    private:
        QSettings *settings;
        u32 delayHit = 0;
    };
}  // namespace EonTimer::Gen4
