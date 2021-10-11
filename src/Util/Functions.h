//
// Created by Dylan Meadows on 2020-03-12.
//

#pragma once

#include "Types.h"
#include <chrono>
#include <string>

namespace EonTimer::Util {
    std::chrono::milliseconds toMinimumLength(std::chrono::milliseconds value);
}  // namespace EonTimer::Util
