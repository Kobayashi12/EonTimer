//
// Created by Dylan Meadows on 2020-03-09.
//

#pragma once

#include <Gen4/TimerModel.h>
#include <QSpinBox>
#include <QWidget>
#include <Timer/Factory/DelayTimer.h>
#include <Util/CalibrationService.h>

namespace EonTimer::Gen4 {
    class TimerPane : public QWidget {
        Q_OBJECT
    public:
        TimerPane(TimerModel *model,
                  const Timer::Factory::DelayTimer *delayTimer,
                  const Util::CalibrationService *calibrationService,
                  QWidget *parent = nullptr);
    signals:
        void timerChanged(std::vector<std::chrono::milliseconds> stages);

    public:
        [[nodiscard]] std::vector<std::chrono::milliseconds> createStages() const;
        void calibrate();

    private:
        void initComponents();

        [[nodiscard]] int getCalibration() const;

    private:
        TimerModel *model;
        const Timer::Factory::DelayTimer *delayTimer;
        const Util::CalibrationService *calibrationService;
    };
}  // namespace EonTimer::Gen4
