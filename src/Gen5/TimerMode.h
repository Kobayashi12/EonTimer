//
// Created by Dylan Meadows on 2020-03-19.
//

#pragma once

#include <QString>
#include <vector>

namespace EonTimer::Gen5 {
    enum TimerMode { STANDARD, C_GEAR, ENTRALINK, ENTRALINK_PLUS };

    const std::vector<TimerMode> &getGen5TimerModes();

    const QString &getName(TimerMode mode);
}  // namespace EonTimer
