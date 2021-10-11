//
// Created by Dylan Meadows on 2020-03-22.
//

#include "EnhancedEntralinkTimer.h"
#include <cmath>

namespace EonTimer::Timer::Factory {
    const double ENTRALINK_FRAME_RATE = 0.837148929;

    EnhancedEntralinkTimer::EnhancedEntralinkTimer(const EntralinkTimer *entralinkTimer)
        : entralinkTimer(entralinkTimer) {}

    std::vector<std::chrono::milliseconds> EnhancedEntralinkTimer::createStages(const u32 targetDelay,
                                                                                const u32 targetSecond,
                                                                                const u32 targetAdvances,
                                                                                const i32 calibration,
                                                                                const i32 entralinkCalibration,
                                                                                const i32 frameCalibration) const {
        return std::vector<std::chrono::milliseconds>{createStage1(targetDelay, targetSecond, calibration),
                                                      createStage2(targetDelay, calibration, entralinkCalibration),
                                                      createStage3(targetAdvances, frameCalibration)};
    }

    std::chrono::milliseconds EnhancedEntralinkTimer::createStage1(const u32 targetDelay,
                                                                   const u32 targetSecond,
                                                                   const i32 calibration) const {
        return entralinkTimer->createStage1(targetDelay, targetSecond, calibration);
    }

    std::chrono::milliseconds EnhancedEntralinkTimer::createStage2(const u32 targetDelay,
                                                                   const i32 calibration,
                                                                   const i32 entralinkCalibration) const {
        return entralinkTimer->createStage2(targetDelay, calibration, entralinkCalibration);
    }

    std::chrono::milliseconds EnhancedEntralinkTimer::createStage3(const u32 targetAdvances,
                                                                   const i32 frameCalibration) const {
        return std::chrono::milliseconds(
            static_cast<i32>(std::round(targetAdvances / ENTRALINK_FRAME_RATE) * 1000 + frameCalibration));
    }

    i32 EnhancedEntralinkTimer::calibrate(u32 targetAdvances, u32 actualAdvances) const {
        return static_cast<i32>((targetAdvances - actualAdvances) / ENTRALINK_FRAME_RATE) * 1000;
    }
}  // namespace EonTimer::Timer::Factory
