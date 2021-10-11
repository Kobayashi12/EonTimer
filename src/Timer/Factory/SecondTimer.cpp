//
// Created by Dylan Meadows on 2020-03-12.
//

#include "SecondTimer.h"
#include "Util/Functions.h"
#include "Util/Types.h"

namespace EonTimer::Timer::Factory {
    std::chrono::milliseconds SecondTimer::createStage1(const i32 targetSecond, const i32 calibration) const {
        return Util::toMinimumLength(std::chrono::milliseconds(targetSecond * 1000 + calibration + 200));
    }

    std::vector<std::chrono::milliseconds> SecondTimer::createStages(const i32 targetSecond,
                                                                     const i32 calibration) const {
        return std::vector<std::chrono::milliseconds>{createStage1(targetSecond, calibration)};
    }

    i32 SecondTimer::calibrate(const i32 targetSecond, const i32 secondHit) const {
        if (secondHit < targetSecond) {
            return (targetSecond - secondHit) * 1000 - 500;
        } else if (secondHit > targetSecond) {
            return (targetSecond - secondHit) * 1000 + 500;
        } else {
            return 0;
        }
    }
}  // namespace EonTimer::Timer::Factory
