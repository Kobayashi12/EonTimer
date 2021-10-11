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
        void sync(QSettings *settings) const;
        [[nodiscard]] i32 getCalibratedDelay() const;
        [[nodiscard]] i32 getCalibratedSecond() const;
        [[nodiscard]] i32 getTargetDelay() const;
        [[nodiscard]] i32 getTargetSecond() const;
        [[nodiscard]] i32 getDelayHit() const;

    signals:
        void calibratedDelayChanged(int calibratedDelay);
        void calibratedSecondChanged(int calibratedSecond);
        void targetDelayChanged(int targetDelay);
        void targetSecondChanged(int targetSecond);
        void delayHitChanged(int delayHit);

    public slots:
        void setCalibratedDelay(int newValue);
        void setCalibratedSecond(int newValue);
        void setTargetDelay(int newValue);
        void setTargetSecond(int newValue);
        void setDelayHit(int newValue);

    private:
        i32 calibratedDelay;
        i32 calibratedSecond;
        i32 targetDelay;
        i32 targetSecond;
        i32 delayHit = 0;
    };
}  // namespace EonTimer::Gen4
