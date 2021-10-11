//
// Created by Dylan Meadows on 2020-03-22.
//

#pragma once

#include "EntralinkTimer.h"
#include <memory>
#include <vector>

namespace EonTimer::Timer::Factory {
    class EnhancedEntralinkTimer {
    public:
        explicit EnhancedEntralinkTimer(const EntralinkTimer *entralinkTimer);
        [[nodiscard]] std::vector<std::chrono::milliseconds> createStages(u32 targetDelay,
                                                                          u32 targetSecond,
                                                                          u32 targetAdvances,
                                                                          i32 calibration,
                                                                          i32 entralinkCalibration,
                                                                          i32 frameCalibration) const;
        [[nodiscard]] std::chrono::milliseconds createStage1(u32 targetDelay, u32 targetSecond, i32 calibration) const;
        [[nodiscard]] std::chrono::milliseconds createStage2(u32 targetDelay,
                                                             i32 calibration,
                                                             i32 entralinkCalibration) const;
        [[nodiscard]] std::chrono::milliseconds createStage3(u32 targetAdvances, i32 frameCalibration) const;
        [[nodiscard]] i32 calibrate(u32 targetAdvances, u32 actualAdvances) const;

    private:
        const EntralinkTimer *entralinkTimer;
    };
}  // namespace EonTimer::Timer::Factory
