//
// Created by Dylan Meadows on 2020-03-22.
//

#pragma once

#include <memory>
#include <vector>

#include "EntralinkTimer.h"

namespace EonTimer::timer {
    class EnhancedEntralinkTimer {
    private:
        const EntralinkTimer *entralinkTimer;

    public:
        explicit EnhancedEntralinkTimer(const EntralinkTimer *entralinkTimer);

        const std::shared_ptr<std::vector<int>> createStages(int targetDelay,
                                                             int targetSecond,
                                                             int targetAdvances,
                                                             int calibration,
                                                             int entralinkCalibration,
                                                             int frameCalibration) const;

        int createStage1(int targetDelay, int targetSecond, int calibration) const;

        int createStage2(int targetDelay, int calibration, int entralinkCalibration) const;

        int createStage3(int targetAdvances, int frameCalibration) const;

        int calibrate(int targetAdvances, int actualAdvances) const;
    };
}  // namespace EonTimer::timer
