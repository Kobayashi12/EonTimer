//
// Created by Dylan Meadows on 2020-03-17.
//

#ifndef EONTIMER_GEN4TIMERMODEL_H
#define EONTIMER_GEN4TIMERMODEL_H

#include <util/Types.h>
#include <QObject>
#include <QSettings>

namespace EonTimer::Gen4 {
    class Gen4TimerModel : public QObject {
        Q_OBJECT
    private:
        i32 calibratedDelay;
        i32 calibratedSecond;
        i32 targetDelay;
        i32 targetSecond;
        i32 delayHit;

    public:
        explicit Gen4TimerModel(QSettings *settings, QObject *parent = nullptr);

        void sync(QSettings *settings) const;

        [[nodiscard]] i32 getCalibratedDelay() const;

        void setCalibratedDelay(i32 newValue);

        [[nodiscard]] i32 getCalibratedSecond() const;

        void setCalibratedSecond(i32 newValue);

        [[nodiscard]] i32 getTargetDelay() const;

        void setTargetDelay(i32 newValue);

        [[nodiscard]] i32 getTargetSecond() const;

        void setTargetSecond(i32 newValue);

        [[nodiscard]] i32 getDelayHit() const;

        void setDelayHit(i32 newValue);

        // @formatter:off
    signals:
        void calibratedDelayChanged(i32 calibratedDelay);
        void calibratedSecondChanged(i32 calibratedSecond);
        void targetDelayChanged(i32 targetDelay);
        void targetSecondChanged(i32 targetSecond);
        void delayHitChanged(i32 delayHit);
        // @formatter:on
    };
}  // namespace EonTimer::timer

#endif  // EONTIMER_GEN4TIMERMODEL_H
