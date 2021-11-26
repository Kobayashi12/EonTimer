//
// Created by Dylan Meadows on 2020-03-12.
//

#include "TimerState.h"

namespace model {
    TimerState::TimerState(const Microseconds &elapsed, const Microseconds &duration)
        : elapsed(elapsed), duration(duration) {}
}  // namespace model
