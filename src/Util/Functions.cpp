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

    bool equalsIgnoreCase(const char *s1, const char *s2) { return equalsIgnoreCase(std::string(s1), std::string(s2)); }

    bool equalsIgnoreCase(const std::string &s1, const std::string &s2) {
        return std::equal(s1.begin(), s1.end(), s2.begin(), s2.end(), [](char c1, char c2) {
            return tolower(c1) == tolower(c2);
        });
    }
}  // namespace EonTimer::Util
