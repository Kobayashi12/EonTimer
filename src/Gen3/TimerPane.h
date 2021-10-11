//
// Created by Dylan Meadows on 2020-03-26.
//

#pragma once

#include "TimerModel.h"
#include <QSpinBox>
#include <QWidget>
#include <Timer/Factory/FrameTimer.h>
#include <Timer/TimerService.h>
#include <Util/CalibrationService.h>
#include <Util/Types.h>

namespace EonTimer::Gen3 {
    class TimerPane : public QWidget {
        Q_OBJECT
    public:
        TimerPane(TimerModel *model,
                  const Timer::Factory::FrameTimer *frameTimer,
                  const Util::CalibrationService *calibrationService,
                  QWidget *parent = nullptr);
        [[nodiscard]] std::vector<std::chrono::milliseconds> createStages() const;
        void calibrate();

    signals:
        void timerChanged(std::vector<std::chrono::milliseconds> stages);

    private:
        void initComponents();
        [[nodiscard]] i32 getCalibration() const;

    private:
        TimerModel *model;
        const Timer::Factory::FrameTimer *frameTimer;
        const Util::CalibrationService *calibrationService;
    };
}  // namespace EonTimer::Gen3
