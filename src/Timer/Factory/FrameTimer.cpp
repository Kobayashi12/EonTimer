//
// Created by Dylan Meadows on 2020-03-20.
//

#include "FrameTimer.h"

namespace EonTimer::Timer::Factory {
    FrameTimer::FrameTimer(const Util::CalibrationService *calibrationService)
        : calibrationService(calibrationService) {}

    std::chrono::milliseconds FrameTimer::createStage2(const u32 targetFrame, const i32 calibration) const {
        return calibrationService->toMilliseconds(targetFrame) + std::chrono::milliseconds(calibration);
    }

    std::vector<std::chrono::milliseconds> FrameTimer::createStages(const std::chrono::milliseconds preTimer,
                                                                    const u32 targetFrame,
                                                                    const i32 calibration) const {
        return std::vector<std::chrono::milliseconds>{preTimer, createStage2(targetFrame, calibration)};
    }

    i32 FrameTimer::calibrate(const u32 targetFrame, const u32 frameHit) const {
        return calibrationService->toMilliseconds(targetFrame - frameHit).count();
    }
}  // namespace EonTimer::Timer::Factory
