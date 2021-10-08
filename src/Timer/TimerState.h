//
// Created by Dylan Meadows on 2020-03-10.
//

#pragma once

#include <chrono>

namespace EonTimer::Timer {
    struct TimerState {
        TimerState(const std::chrono::milliseconds &duration, const std::chrono::milliseconds &remaining);

        const std::chrono::milliseconds &duration;
        const std::chrono::milliseconds &remaining;
    };
}  // namespace EonTimer
