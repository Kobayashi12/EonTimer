//
// Created by Dylan Meadows on 2020-03-19.
//

#ifndef EONTIMER_GEN5TIMERMODE_H
#define EONTIMER_GEN5TIMERMODE_H

#include <vector>
#include <string>

namespace EonTimer {
    enum Gen5TimerMode { STANDARD, C_GEAR, ENTRALINK, ENTRALINK_PLUS, COUNT };

    const std::vector<Gen5TimerMode> &getGen5TimerModes();

    const std::string &getName(Gen5TimerMode mode);
}  // namespace EonTimer

#endif  // EONTIMER_GEN5TIMERMODE_H
