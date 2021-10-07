//
// Created by Dylan Meadows on 2020-03-26.
//

#ifndef EONTIMER_GEN3TIMERPANE_H
#define EONTIMER_GEN3TIMERPANE_H

#include <gen3/Gen3TimerModel.h>
#include <services/CalibrationService.h>
#include <services/TimerService.h>
#include <services/timers/FrameTimer.h>
#include <util/Types.h>

#include <QSpinBox>
#include <QWidget>

namespace EonTimer::Gen3 {
    class Gen3TimerPane : public QWidget {
        Q_OBJECT
    private:
        Gen3TimerModel *model;
        const EonTimer::timer::FrameTimer *frameTimer;
        const EonTimer::CalibrationService *calibrationService;

    public:
        Gen3TimerPane(Gen3TimerModel *model,
                      const EonTimer::timer::FrameTimer *frameTimer,
                      const EonTimer::CalibrationService *calibrationService,
                      QWidget *parent = nullptr);

        std::shared_ptr<std::vector<int>> createStages();

        void calibrate();

    private:
        void initComponents();

        i32 getCalibration() const;

    signals:
        void timerChanged(std::shared_ptr<std::vector<i32>> stages);
    };
}  // namespace gui::timer

#endif  // EONTIMER_GEN3TIMERPANE_H
