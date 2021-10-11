//
// Created by Dylan Meadows on 2020-03-12.
//

#include "DelayTimer.h"
#include <Util/Functions.h>

namespace EonTimer::Timer::Factory {
    const i32 CLOSE_THRESHOLD = 167;
    const double UPDATE_FACTOR = 1.0;
    const double CLOSE_UPDATE_FACTOR = 0.75;

    DelayTimer::DelayTimer(const SecondTimer *secondTimer, const Util::CalibrationService *calibrationService)
        : secondTimer(secondTimer), calibrationService(calibrationService) {}

    std::chrono::milliseconds DelayTimer::createStage1(const i32 targetDelay,
                                                       const i32 targetSecond,
                                                       const i32 calibration) const {
        return Util::toMinimumLength(secondTimer->createStage1(targetSecond, calibration) -
                                     calibrationService->toMilliseconds(targetDelay));
    }

    std::chrono::milliseconds DelayTimer::createStage2(const i32 targetDelay, const i32 calibration) const {
        return calibrationService->toMilliseconds(targetDelay) - std::chrono::milliseconds(calibration);
    }

    std::vector<std::chrono::milliseconds> DelayTimer::createStages(const i32 targetDelay,
                                                                    const i32 targetSecond,
                                                                    const i32 calibration) const {
        return std::vector<std::chrono::milliseconds>{createStage1(targetDelay, targetSecond, calibration),
                                                      createStage2(targetDelay, calibration)};
    }

    i32 DelayTimer::calibrate(const i32 targetDelay, const i32 delayHit) const {
        const auto delta =
            calibrationService->toMilliseconds(delayHit) - calibrationService->toMilliseconds(targetDelay);
        if (std::abs(delta.count()) <= CLOSE_THRESHOLD) {
            return static_cast<i32>(CLOSE_UPDATE_FACTOR * delta.count());
        } else {
            return static_cast<i32>(UPDATE_FACTOR * delta.count());
        }
    }
}  // namespace EonTimer::Timer::Factory
