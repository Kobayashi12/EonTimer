//
// Created by Dylan Meadows on 2020-03-10.
//

#include "CalibrationService.h"

#include <cmath>

namespace EonTimer {
    CalibrationService::CalibrationService(const Timer::TimerSettingsModel *timerSettings)
        : timerSettings(timerSettings) {}

    i32 CalibrationService::toDelays(const i32 milliseconds) const {
        const double framerate = Timer::getFrameRate(timerSettings->getConsole());
        return static_cast<i32>(std::round(milliseconds / framerate));
    }

    i32 CalibrationService::toMilliseconds(const i32 delays) const {
        const double framerate = Timer::getFrameRate(timerSettings->getConsole());
        return static_cast<i32>(std::round(delays * framerate));
    }

    i32 CalibrationService::calibrateToDelays(const i32 milliseconds) const {
        return timerSettings->isPrecisionCalibrationEnabled() ? milliseconds : toDelays(milliseconds);
    }

    i32 CalibrationService::calibrateToMilliseconds(i32 delays) const {
        return timerSettings->isPrecisionCalibrationEnabled() ? delays : toMilliseconds(delays);
    }

    i32 CalibrationService::createCalibration(const i32 delays, const i32 seconds) const {
        return toMilliseconds(delays - toDelays(seconds * 1000));
    }
}  // namespace EonTimer