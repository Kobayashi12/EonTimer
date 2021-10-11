//
// Created by Dylan Meadows on 2020-03-20.
//

#include "FrameTimer.h"

namespace EonTimer::Timer::Factory {
    FrameTimer::FrameTimer(const Util::CalibrationService *calibrationService)
        : calibrationService(calibrationService) {}

    std::chrono::milliseconds FrameTimer::createStage1(const i32 preTimer) const {
        return std::chrono::milliseconds(preTimer);
    }

    std::chrono::milliseconds FrameTimer::createStage2(const i32 targetFrame, const i32 calibration) const {
        return calibrationService->toMilliseconds(targetFrame) + std::chrono::milliseconds(calibration);
    }

    std::vector<std::chrono::milliseconds> FrameTimer::createStages(const i32 preTimer,
                                                                    const i32 targetFrame,
                                                                    const i32 calibration) const {
        return std::vector<std::chrono::milliseconds>{createStage1(preTimer), createStage2(targetFrame, calibration)};
    }

    i32 FrameTimer::calibrate(const i32 targetFrame, const i32 frameHit) const {
        return calibrationService->toMilliseconds(targetFrame - frameHit).count();
    }
}  // namespace EonTimer::Timer::Factory
