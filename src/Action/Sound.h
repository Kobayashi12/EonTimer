//
// Created by Dylan Meadows on 2020-03-14.
//

#pragma once

#include <vector>
#include <QString>

namespace EonTimer::Action {
    enum Sound { BEEP, DING, POP, TICK };

    const std::vector<Sound> &getSounds();

    const QString &getName(Sound sound);
}  // namespace EonTimer
