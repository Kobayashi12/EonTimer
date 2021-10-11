//
// Created by Dylan Meadows on 2020-03-12.
//

#pragma once

#include <chrono>
#include <string>

namespace EonTimer::Util {
    std::chrono::milliseconds toMinimumLength(std::chrono::milliseconds value);

    bool equalsIgnoreCase(const char *s1, const char *s2);

    bool equalsIgnoreCase(const std::string &s1, const std::string &s2);
}  // namespace EonTimer::Util
