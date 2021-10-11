//
// Created by Dylan Meadows on 2020-03-10.
//

#pragma once

#include <Timer/Settings.h>
#include <Util/Types.h>

namespace EonTimer::Util {
    class CalibrationService {
    public:
        explicit CalibrationService(const Timer::Settings *timerSettings);
        [[nodiscard]] i32 toDelays(std::chrono::milliseconds ms) const;
        [[nodiscard]] std::chrono::milliseconds toMilliseconds(i32 delays) const;
        [[nodiscard]] i32 calibrateToDelays(std::chrono::milliseconds ms) const;
        [[nodiscard]] std::chrono::milliseconds calibrateToMilliseconds(i32 delays) const;
        [[nodiscard]] i32 createCalibration(i32 delays, i32 seconds) const;

    private:
        const Timer::Settings *timerSettings;
    };
}  // namespace EonTimer::Util
