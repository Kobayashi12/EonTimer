//
// Created by Dylan Meadows on 2020-03-10.
//

#pragma once

#include <Timer/TimerSettingsModel.h>
#include <Util/Types.h>

namespace EonTimer {
    class CalibrationService {
    private:
        const Timer::TimerSettingsModel *timerSettings;

    public:
        explicit CalibrationService(const Timer::TimerSettingsModel *timerSettings);

        [[nodiscard]] i32 toDelays(i32 milliseconds) const;

        [[nodiscard]] i32 toMilliseconds(i32 delays) const;

        [[nodiscard]] i32 calibrateToDelays(i32 milliseconds) const;

        [[nodiscard]] i32 calibrateToMilliseconds(i32 delays) const;

        [[nodiscard]] i32 createCalibration(i32 delays, i32 seconds) const;
    };
}  // namespace EonTimer
