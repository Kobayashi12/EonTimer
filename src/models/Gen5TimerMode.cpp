//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerMode.h"

namespace EonTimer {
    const std::vector<Gen5TimerMode> &getGen5TimerModes() {
        static const std::vector<Gen5TimerMode> values{STANDARD, C_GEAR, ENTRALINK, ENTRALINK_PLUS};
        return values;
    }

    const std::string &getName(const Gen5TimerMode mode) {
        static const std::string names[COUNT] = {"Standard", "C-Gear", "Entralink", "Entralink+"};
        return names[mode];
    }
}  // namespace EonTimer
