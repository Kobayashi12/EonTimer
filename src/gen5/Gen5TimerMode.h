//
// Created by Dylan Meadows on 2020-03-19.
//

#ifndef EONTIMER_GEN5TIMERMODE_H
#define EONTIMER_GEN5TIMERMODE_H

#include <QString>
#include <vector>

namespace EonTimer::Gen5 {
    enum Gen5TimerMode { STANDARD, C_GEAR, ENTRALINK, ENTRALINK_PLUS, COUNT };

    const std::vector<Gen5TimerMode> &getGen5TimerModes();

    const QString &getName(Gen5TimerMode mode);
}  // namespace EonTimer

#endif  // EONTIMER_GEN5TIMERMODE_H
