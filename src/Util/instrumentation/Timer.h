//
// Created by dmeadows on 5/2/20.
//

#pragma once

#include <chrono>

namespace EonTimer::Instrumentation {
    class Timer {
    public:
        explicit Timer(const char *operation);

        ~Timer();

    private:
        const char *operation;
        std::chrono::time_point<std::chrono::high_resolution_clock> startTimePoint;
        bool running;

        void stop();
    };
}  // namespace Util::instrumentation
