//
// Created by Dylan Meadows on 2020-03-19.
//

#pragma once

#include <QString>
#include <vector>

namespace EonTimer::Gen5 {
    enum Gen5TimerMode { STANDARD, C_GEAR, ENTRALINK, ENTRALINK_PLUS };

    const std::vector<Gen5TimerMode> &getGen5TimerModes();

    const QString &getName(Gen5TimerMode mode);
}  // namespace EonTimer
