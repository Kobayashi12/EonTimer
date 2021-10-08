//
// Created by dmeadows on 5/2/20.
//

#include "Timer.h"

#include <iostream>

namespace EonTimer::Instrumentation {
    using namespace std::chrono;

    Timer::Timer(const char *operation) : operation(operation) {
        startTimePoint = high_resolution_clock::now();
        running = true;
    }

    Timer::~Timer() { stop(); }

    void Timer::stop() {
        if (running) {
            auto now = high_resolution_clock::now();
            auto elapsed = high_resolution_clock::now() - startTimePoint;
            long elapsed_us = duration_cast<microseconds>(elapsed).count();
            long elapsed_ms = duration_cast<milliseconds>(elapsed).count();

            std::cout << operation << ": ";
            std::cout << elapsed_us << "us (" << elapsed_ms << "ms)\n";
            running = false;
        }
    }
}  // namespace Util::instrumentation
