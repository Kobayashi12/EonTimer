//
// Created by dmeadows on 5/2/20.
//

#include "Clock.h"

#include <iostream>

namespace EonTimer::Timer {
    Clock::Clock() { lastTick = std::chrono::high_resolution_clock::now(); }

    std::chrono::microseconds Clock::tick() {
        auto previousTick = lastTick;
        lastTick = std::chrono::high_resolution_clock::now();
        return std::chrono::duration_cast<std::chrono::microseconds>(lastTick - previousTick);
    }
}  // namespace Util
