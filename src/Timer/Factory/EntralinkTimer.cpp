//
// Created by Dylan Meadows on 2020-03-20.
//

#include "EntralinkTimer.h"

using namespace std::literals::chrono_literals;

namespace EonTimer::Timer::Factory {
    EntralinkTimer::EntralinkTimer(const DelayTimer *delayTimer) : delayTimer(delayTimer) {}

    std::chrono::milliseconds EntralinkTimer::createStage1(const u32 targetDelay,
                                                           const u32 targetSecond,
                                                           const i32 calibration) const {
        return delayTimer->createStage1(targetDelay, targetSecond, calibration) + 250ms;
    }

    std::chrono::milliseconds EntralinkTimer::createStage2(const u32 targetDelay,
                                                           const i32 calibration,
                                                           const i32 entralinkCalibration) const {
        return delayTimer->createStage2(targetDelay, calibration) - std::chrono::milliseconds(entralinkCalibration);
    }

    std::vector<std::chrono::milliseconds> EntralinkTimer::createStages(const u32 targetDelay,
                                                                        const u32 targetSecond,
                                                                        const i32 calibration,
                                                                        const i32 entralinkCalibration) const {
        return std::vector<std::chrono::milliseconds>{createStage1(targetDelay, targetSecond, calibration),
                                                      createStage2(targetDelay, calibration, entralinkCalibration)};
    }

    i32 EntralinkTimer::calibrate(const u32 targetDelay, const u32 delayHit) const {
        return delayTimer->calibrate(targetDelay, delayHit);
    }
}  // namespace EonTimer::Timer::Factory
