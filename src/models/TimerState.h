//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERSTATE_H
#define EONTIMER_TIMERSTATE_H

#include <chrono>

namespace EonTimer {
    struct TimerState {
        TimerState(const std::chrono::milliseconds &duration, const std::chrono::milliseconds &remaining);

        const std::chrono::milliseconds &duration;
        const std::chrono::milliseconds &remaining;
    };
}  // namespace EonTimer

#endif  // EONTIMER_TIMERSTATE_H
