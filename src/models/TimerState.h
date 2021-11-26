//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERSTATE_H
#define EONTIMER_TIMERSTATE_H

#include <util/Types.h>

#include <chrono>

namespace model {
    struct TimerState {
        const Microseconds &elapsed;
        const Microseconds &duration;
        TimerState(const Microseconds &elapsed, const Microseconds &duration);
    };
}  // namespace model

#endif  // EONTIMER_TIMERSTATE_H
