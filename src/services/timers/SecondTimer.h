//
// Created by Dylan Meadows on 2020-03-12.
//

#pragma once

#include <memory>
#include <vector>

namespace EonTimer::timer {
    class SecondTimer {
    public:
        const std::shared_ptr<std::vector<int>> createStages(int targetSecond, int calibration) const;

        int createStage1(int targetSecond, int calibration) const;

        int calibrate(int targetSecond, int secondHit) const;
    };
}  // namespace EonTimer::timer
