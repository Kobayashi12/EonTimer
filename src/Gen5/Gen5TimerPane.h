//
// Created by Dylan Meadows on 2020-03-19.
//

#pragma once

#include <Gen5/Gen5TimerModel.h>
#include <Timer/TimerService.h>
#include <Util/FieldSet.h>
#include <Util/Types.h>
#include <services/CalibrationService.h>
#include <services/timers/DelayTimer.h>
#include <services/timers/EnhancedEntralinkTimer.h>
#include <services/timers/EntralinkTimer.h>
#include <services/timers/SecondTimer.h>

#include <QComboBox>
#include <QGridLayout>
#include <QSpinBox>
#include <QWidget>

namespace EonTimer::Gen5 {
    class Gen5TimerPane : public QWidget {
        Q_OBJECT
    public:
        explicit Gen5TimerPane(Gen5TimerModel *model,
                               const EonTimer::timer::DelayTimer *delayTimer,
                               const EonTimer::timer::SecondTimer *secondTimer,
                               const EonTimer::timer::EntralinkTimer *entralinkTimer,
                               const EonTimer::timer::EnhancedEntralinkTimer *enhancedEntralinkTimer,
                               const EonTimer::CalibrationService *calibrationService,
                               QWidget *parent = nullptr);

        std::shared_ptr<std::vector<i32>> createStages();

        void calibrate();

    private:
        void initComponents();

        [[nodiscard]] i32 getDelayCalibration() const;

        [[nodiscard]] i32 getSecondCalibration() const;

        [[nodiscard]] i32 getEntralinkCalibration() const;

        [[nodiscard]] i32 getAdvancesCalibration() const;

        // @formatter:off
    signals:
        void timerChanged(std::shared_ptr<std::vector<i32>> stages);
        // @formatter:on

    private:
        Gen5TimerModel *model;
        const EonTimer::timer::DelayTimer *delayTimer;
        const EonTimer::timer::SecondTimer *secondTimer;
        const EonTimer::timer::EntralinkTimer *entralinkTimer;
        const EonTimer::timer::EnhancedEntralinkTimer *enhancedEntralinkTimer;
        const EonTimer::CalibrationService *calibrationService;
    };
}  // namespace EonTimer::Gen5
