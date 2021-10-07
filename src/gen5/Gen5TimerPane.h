//
// Created by Dylan Meadows on 2020-03-19.
//

#ifndef EONTIMER_GEN5TIMERPANE_H
#define EONTIMER_GEN5TIMERPANE_H

#include <gen5/Gen5TimerModel.h>
#include <gui/util/FieldSet.h>
#include <services/CalibrationService.h>
#include <services/TimerService.h>
#include <services/timers/DelayTimer.h>
#include <services/timers/EnhancedEntralinkTimer.h>
#include <services/timers/EntralinkTimer.h>
#include <services/timers/SecondTimer.h>
#include <util/Types.h>

#include <QComboBox>
#include <QGridLayout>
#include <QSpinBox>
#include <QWidget>

namespace EonTimer::Gen5 {
    class Gen5TimerPane : public QWidget {
        Q_OBJECT
    private:
        Gen5TimerModel *model;
        const EonTimer::timer::DelayTimer *delayTimer;
        const EonTimer::timer::SecondTimer *secondTimer;
        const EonTimer::timer::EntralinkTimer *entralinkTimer;
        const EonTimer::timer::EnhancedEntralinkTimer *enhancedEntralinkTimer;
        const EonTimer::CalibrationService *calibrationService;

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

        int getDelayCalibration() const;

        int getSecondCalibration() const;

        int getEntralinkCalibration() const;

        int getAdvancesCalibration() const;

        // @formatter:off
    signals:
        void timerChanged(std::shared_ptr<std::vector<i32>> stages);
        // @formatter:on
    };
}  // namespace gui::timer

#endif  // EONTIMER_GEN5TIMERPANE_H
