//
// Created by dmeadows on 5/2/20.
//

#pragma once

#include <chrono>

namespace EonTimer::Timer {
    class Clock {
    public:
        Clock();
        std::chrono::microseconds tick();

    private:
        std::chrono::time_point<std::chrono::high_resolution_clock> lastTick;
    };
}  // namespace EonTimer::Timer
