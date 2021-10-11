//
// Created by Dylan Meadows on 2020-03-12.
//

#include "Functions.h"

namespace EonTimer::Util {
    static std::chrono::minutes oneMinute() {
        static const std::chrono::minutes oneMinute(1);
        return oneMinute;
    }

    static std::chrono::milliseconds minimumLength() {
        static const std::chrono::milliseconds minimumLength(14000);
        return minimumLength;
    }

    std::chrono::milliseconds toMinimumLength(const std::chrono::milliseconds value) {
        std::chrono::milliseconds normalized = value;
        while (normalized < minimumLength()) {
            normalized += oneMinute();
        }
        return normalized;
    }
}  // namespace EonTimer::Util
