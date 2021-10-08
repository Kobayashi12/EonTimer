//
// Created by Dylan Meadows on 2020-03-26.
//

#pragma once

#include <Timer/TimerService.h>
#include <Util/Types.h>
#include <services/CalibrationService.h>
#include <services/timers/FrameTimer.h>

#include <QSpinBox>
#include <QWidget>

#include "Gen3TimerModel.h"

namespace EonTimer::Gen3 {
    class Gen3TimerPane : public QWidget {
        Q_OBJECT
    public:
        Gen3TimerPane(Gen3TimerModel *model,
                      const timer::FrameTimer *frameTimer,
                      const CalibrationService *calibrationService,
                      QWidget *parent = nullptr);

        std::shared_ptr<std::vector<int>> createStages();

        void calibrate();

    private:
        void initComponents();

        [[nodiscard]] i32 getCalibration() const;

    signals:
        void timerChanged(std::shared_ptr<std::vector<i32>> stages);

    private:
        Gen3TimerModel *model;
        const timer::FrameTimer *frameTimer;
        const CalibrationService *calibrationService;
    };
}  // namespace EonTimer::Gen3
