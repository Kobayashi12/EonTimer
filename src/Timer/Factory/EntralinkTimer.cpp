//
// Created by Dylan Meadows on 2020-03-20.
//

#include "EntralinkTimer.h"

using namespace std::literals::chrono_literals;

namespace EonTimer::Timer::Factory {
    EntralinkTimer::EntralinkTimer(const DelayTimer *delayTimer) : delayTimer(delayTimer) {}

    std::chrono::milliseconds EntralinkTimer::createStage1(const int targetDelay,
                                                           const int targetSecond,
                                                           const int calibration) const {
        return delayTimer->createStage1(targetDelay, targetSecond, calibration) + 250ms;
    }

    std::chrono::milliseconds EntralinkTimer::createStage2(const int targetDelay,
                                                           const int calibration,
                                                           const int entralinkCalibration) const {
        return delayTimer->createStage2(targetDelay, calibration) - std::chrono::milliseconds(entralinkCalibration);
    }

    std::vector<std::chrono::milliseconds> EntralinkTimer::createStages(const int targetDelay,
                                                                        const int targetSecond,
                                                                        const int calibration,
                                                                        const int entralinkCalibration) const {
        return std::vector<std::chrono::milliseconds>{createStage1(targetDelay, targetSecond, calibration),
                                                      createStage2(targetDelay, calibration, entralinkCalibration)};
    }

    int EntralinkTimer::calibrate(const int targetDelay, const int delayHit) const {
        return delayTimer->calibrate(targetDelay, delayHit);
    }
}  // namespace EonTimer::Timer::Factory
