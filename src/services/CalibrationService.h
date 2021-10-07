//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_CALIBRATIONSERVICE_H
#define EONTIMER_CALIBRATIONSERVICE_H

#include <util/Types.h>
#include <models/settings/TimerSettingsModel.h>

namespace EonTimer {
    class CalibrationService {
    private:
        const EonTimer::settings::TimerSettingsModel *timerSettings;

    public:
        explicit CalibrationService(const EonTimer::settings::TimerSettingsModel *timerSettings);

        [[nodiscard]] i32 toDelays(i32 milliseconds) const;

        [[nodiscard]] i32 toMilliseconds(i32 delays) const;

        [[nodiscard]] i32 calibrateToDelays(i32 milliseconds) const;

        [[nodiscard]] i32 calibrateToMilliseconds(i32 delays) const;

        [[nodiscard]] i32 createCalibration(i32 delays, i32 seconds) const;
    };
}  // namespace EonTimer

#endif  // EONTIMER_CALIBRATIONSERVICE_H
