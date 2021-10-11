//
// Created by Dylan Meadows on 2020-03-12.
//

#pragma once

#include "SecondTimer.h"
#include "Util/CalibrationService.h"
#include <memory>
#include <vector>

namespace EonTimer::Timer::Factory {
    class DelayTimer {
    public:
        explicit DelayTimer(const SecondTimer *secondTimer, const Util::CalibrationService *calibrationService);
        [[nodiscard]] std::chrono::milliseconds createStage1(u32 targetDelay, u32 targetSecond, i32 calibration) const;
        [[nodiscard]] std::chrono::milliseconds createStage2(u32 targetDelay, i32 calibration) const;
        [[nodiscard]] std::vector<std::chrono::milliseconds> createStages(u32 targetDelay,
                                                                          u32 targetSecond,
                                                                          i32 calibration) const;
        [[nodiscard]] i32 calibrate(u32 targetDelay, u32 delayHit) const;

    private:
        const SecondTimer *secondTimer;
        const Util::CalibrationService *calibrationService;
    };
}  // namespace EonTimer::Timer::Factory
