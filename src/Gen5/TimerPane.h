//
// Created by Dylan Meadows on 2020-03-19.
//

#pragma once

#include <Gen5/TimerModel.h>
#include <QComboBox>
#include <QGridLayout>
#include <QSpinBox>
#include <QWidget>
#include <Timer/Factory/DelayTimer.h>
#include <Timer/Factory/EnhancedEntralinkTimer.h>
#include <Timer/Factory/EntralinkTimer.h>
#include <Timer/Factory/SecondTimer.h>
#include <Timer/TimerService.h>
#include <Util/CalibrationService.h>
#include <Util/FieldSet.h>
#include <Util/Types.h>

namespace EonTimer::Gen5 {
    class TimerPane : public QWidget {
        Q_OBJECT
    public:
        TimerPane(TimerModel *model,
                  const Timer::Factory::DelayTimer *delayTimer,
                  const Timer::Factory::SecondTimer *secondTimer,
                  const Timer::Factory::EntralinkTimer *entralinkTimer,
                  const Timer::Factory::EnhancedEntralinkTimer *enhancedEntralinkTimer,
                  const Util::CalibrationService *calibrationService,
                  QWidget *parent = nullptr);
        std::vector<std::chrono::milliseconds> createStages() const;
        void calibrate();

    signals:
        void timerChanged(std::vector<std::chrono::milliseconds> stages);

    private:
        void initComponents();
        [[nodiscard]] i32 getDelayCalibration() const;
        [[nodiscard]] i32 getSecondCalibration() const;
        [[nodiscard]] i32 getEntralinkCalibration() const;
        [[nodiscard]] i32 getAdvancesCalibration() const;

    private:
        TimerModel *model;
        const Timer::Factory::DelayTimer *delayTimer;
        const Timer::Factory::SecondTimer *secondTimer;
        const Timer::Factory::EntralinkTimer *entralinkTimer;
        const Timer::Factory::EnhancedEntralinkTimer *enhancedEntralinkTimer;
        const Util::CalibrationService *calibrationService;
    };
}  // namespace EonTimer::Gen5
