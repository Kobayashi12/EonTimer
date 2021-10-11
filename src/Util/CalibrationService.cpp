//
// Created by Dylan Meadows on 2020-03-10.
//

#include "CalibrationService.h"
#include <cmath>

namespace EonTimer::Util {
    CalibrationService::CalibrationService(const Timer::Settings *timerSettings) : timerSettings(timerSettings) {}

    i32 CalibrationService::toDelays(const std::chrono::milliseconds ms) const {
        const double framerate = Timer::getFrameRate(timerSettings->getConsole());
        return static_cast<i32>(std::round(ms.count() / framerate));
    }

    std::chrono::milliseconds CalibrationService::toMilliseconds(const i32 delays) const {
        const double framerate = Timer::getFrameRate(timerSettings->getConsole());
        return std::chrono::milliseconds(static_cast<i32>(std::round(delays * framerate)));
    }

    i32 CalibrationService::calibrateToDelays(const std::chrono::milliseconds ms) const {
        return timerSettings->isPrecisionCalibrationEnabled() ? ms.count() : toDelays(ms);
    }

    std::chrono::milliseconds CalibrationService::calibrateToMilliseconds(i32 delays) const {
        return timerSettings->isPrecisionCalibrationEnabled() ? std::chrono::milliseconds(delays)
                                                              : toMilliseconds(delays);
    }

    i32 CalibrationService::createCalibration(const i32 delays, const i32 seconds) const {
        return toMilliseconds(delays - toDelays(std::chrono::milliseconds(seconds * 1000))).count();
    }
}  // namespace EonTimer::Util