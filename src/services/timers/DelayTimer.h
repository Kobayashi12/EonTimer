//
// Created by Dylan Meadows on 2020-03-12.
//

#pragma once

#include <services/CalibrationService.h>

#include <memory>
#include <vector>

#include "SecondTimer.h"

namespace EonTimer::timer {
    class DelayTimer {
    private:
        const SecondTimer *secondTimer;
        const CalibrationService *calibrationService;

    public:
        DelayTimer(const SecondTimer *secondTimer, const CalibrationService *calibrationService);

        const std::shared_ptr<std::vector<int>> createStages(int targetDelay, int targetSecond, int calibration) const;

        int createStage1(int targetDelay, int targetSecond, int calibration) const;

        int createStage2(int targetDelay, int calibration) const;

        int calibrate(int targetDelay, int delayHit) const;
    };
}  // namespace EonTimer::timer
