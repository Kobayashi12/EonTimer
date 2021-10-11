//
// Created by Dylan Meadows on 2020-03-12.
//

#pragma once

#include "Util/Types.h"
#include <chrono>
#include <vector>

namespace EonTimer::Timer::Factory {
    class SecondTimer {
    public:
        [[nodiscard]] std::chrono::milliseconds createStage1(u32 targetSecond, i32 calibration) const;
        [[nodiscard]] std::vector<std::chrono::milliseconds> createStages(u32 targetSecond, i32 calibration) const;
        [[nodiscard]] i32 calibrate(u32 targetSecond, u32 secondHit) const;
    };
}  // namespace EonTimer::Timer::Factory
