//
// Created by dmeadows on 5/2/20.
//

#include "Clock.h"

#include <iostream>

namespace EonTimer {
    using namespace std::chrono;

    Clock::Clock() { lastTick = high_resolution_clock::now(); }

    microseconds Clock::tick() {
        auto previousTick = lastTick;
        lastTick = std::chrono::high_resolution_clock::now();
        return std::chrono::duration_cast<std::chrono::microseconds>(lastTick - previousTick);
    }
}  // namespace util
