//
// Created by Dylan Meadows on 2020-03-19.
//

#include "TimerMode.h"

namespace EonTimer::Gen5 {
    const std::vector<TimerMode> &getGen5TimerModes() {
        static const std::vector<TimerMode> values{STANDARD, C_GEAR, ENTRALINK, ENTRALINK_PLUS};
        return values;
    }

    const QString &getName(const TimerMode mode) {
        static const std::vector<QString> names{"Standard", "C-Gear", "Entralink", "Entralink+"};
        return names[mode];
    }
}  // namespace EonTimer
