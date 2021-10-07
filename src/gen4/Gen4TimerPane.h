//
// Created by Dylan Meadows on 2020-03-09.
//

#ifndef EONTIMER_GEN4TIMERPANE_H
#define EONTIMER_GEN4TIMERPANE_H

#include <gen4/Gen4TimerModel.h>
#include <services/CalibrationService.h>
#include <services/timers/DelayTimer.h>

#include <QSpinBox>
#include <QWidget>

namespace EonTimer::Gen4 {
    class Gen4TimerPane : public QWidget {
        Q_OBJECT
    private:
        Gen4TimerModel *model;
        const EonTimer::timer::DelayTimer *delayTimer;
        const EonTimer::CalibrationService *calibrationService;

    public:
        Gen4TimerPane(Gen4TimerModel *model,
                      const EonTimer::timer::DelayTimer *delayTimer,
                      const EonTimer::CalibrationService *calibrationService,
                      QWidget *parent = nullptr);

        std::shared_ptr<std::vector<int>> createStages();

        void calibrate();

    private:
        void initComponents();

        int getCalibration() const;

    signals:
        void timerChanged(std::shared_ptr<std::vector<int>> stages);
    };
}  // namespace gui::timer

#endif  // EONTIMER_GEN4TIMERPANE_H
