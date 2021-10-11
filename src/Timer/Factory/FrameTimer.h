//
// Created by Dylan Meadows on 2020-03-20.
//

#pragma once

#include "Util/CalibrationService.h"
#include <memory>
#include <vector>

namespace EonTimer::Timer::Factory {
    class FrameTimer {
    public:
        explicit FrameTimer(const Util::CalibrationService *calibrationService);
        [[nodiscard]] std::chrono::milliseconds createStage2(u32 targetFrame, i32 calibration) const;
        [[nodiscard]] std::vector<std::chrono::milliseconds> createStages(std::chrono::milliseconds preTimer,
                                                                          u32 targetFrame,
                                                                          i32 calibration) const;
        [[nodiscard]] i32 calibrate(u32 targetFrame, u32 frameHit) const;

    private:
        const Util::CalibrationService *calibrationService;
    };
}  // namespace EonTimer::Timer::Factory
