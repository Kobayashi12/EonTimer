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
        [[nodiscard]] std::chrono::milliseconds createStage1(u32 targetDelay, u32 targetSecond, i32 calibration) const;
        [[nodiscard]] std::chrono::milliseconds createStage2(u32 targetDelay,
                                                             i32 calibration,
                                                             i32 entralinkCalibration) const;
        [[nodiscard]] std::vector<std::chrono::milliseconds> createStages(u32 targetDelay,
                                                                          u32 targetSecond,
                                                                          i32 calibration,
                                                                          i32 entralinkCalibration) const;
        [[nodiscard]] i32 calibrate(u32 targetDelay, u32 delayHit) const;

    private:
        const DelayTimer *delayTimer;
    };
}  // namespace EonTimer::Timer::Factory
