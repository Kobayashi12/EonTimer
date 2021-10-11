//
// Created by Dylan Meadows on 2020-03-20.
//

#pragma once

#include "DelayTimer.h"
#include <memory>
#include <vector>

namespace EonTimer::Timer::Factory {
    class EntralinkTimer {
    public:
        explicit EntralinkTimer(const DelayTimer *delayTimer);
        [[nodiscard]] std::chrono::milliseconds createStage1(i32 targetDelay, i32 targetSecond, i32 calibration) const;
        [[nodiscard]] std::chrono::milliseconds createStage2(i32 targetDelay,
                                                             i32 calibration,
                                                             i32 entralinkCalibration) const;
        [[nodiscard]] std::vector<std::chrono::milliseconds> createStages(i32 targetDelay,
                                                                          i32 targetSecond,
                                                                          i32 calibration,
                                                                          i32 entralinkCalibration) const;
        [[nodiscard]] i32 calibrate(i32 targetDelay, i32 delayHit) const;

    private:
        const DelayTimer *delayTimer;
    };
}  // namespace EonTimer::Timer::Factory
